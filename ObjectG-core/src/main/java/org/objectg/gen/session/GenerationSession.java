package org.objectg.gen.session;

import java.util.WeakHashMap;

import org.objectg.conf.GenerationConfiguration;
import org.objectg.gen.CompositeGenerator;
import org.objectg.gen.GenerationContext;
import org.objectg.gen.impl.Generators;
import org.objectg.util.Types;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * <p>
 * GenerationSession represents unit of work when generating objects.
 * </p>
 * <p>
 * During GenerationSession multiple different objects can be created. GenerationSession makes sure that each
 * generation is reproducible given that code used to generate objects remained unchanged.
 * </p>
 * <p>
 * GenerationSession manages generators and sequence that will be used in given thread. This simplifies all other
 * code, because it can be not thread-safe. All state should be managed in bounds of GenerationSession. This mean
 * that any {@link org.objectg.gen.Generator} or {@link org.objectg.gen.GenerationRule} should NOT maintain it's state between different
 * GenerationSessions. This is important, because if state is maintained between different GenerationSession then
 * result of generation is affected by previous generations. State that is considered
 * </p>
 * <p>
 * GenerationSession provides factory methods for creating {@link org.objectg.gen.ValueSequence} for managing sequence of values
 * in the scope of GenerationSession.
 * </p>
 * <p>
 * User: __nocach
 * Date: 22.1.13
 * </p>
 */
public class GenerationSession {
	private static final Logger logger = LoggerFactory.getLogger(GenerationSession.class);
	private static ThreadLocal<GenerationSession> threadInstance = new ThreadLocal<GenerationSession>();
	private static SessionStateLifeCycle sessionStateLifeCycle = new SessionStateLifeCycle();
	/**
	 * create value sequences relevant to this session
	 */
	private SessionSequenceFactory sequenceFactory = new SessionSequenceFactory();
	/**
	 * maintain state of the current session. Key is object owning state, Value is saved state of the owning object.
	 */
	private WeakHashMap<Object, Object> managedState = new WeakHashMap<Object, Object>();
	/**
	 * state of the current Generation Session
	 */
	private State currentState = State.UNDEFINED;
	/**
	 * generators to be used to generation values
	 */
	private CompositeGenerator generatorChain = null;

	private GenerationSession() {
	}

	/**
	 * generate object in this GenerationSession
	 *
	 * @param context not null context for which generator must be found
	 * @throws IllegalArgumentException if generator for the passed context and configuration was not found
	 */
	public <T> T generate(GenerationConfiguration configuration, GenerationContext context) {
		Assert.notNull(context, "context");

		T result = (T) generatorChain.generate(configuration, context);
		if (logger.isDebugEnabled()) {
			logger.debug("generated value=" + getSafeObjectToString(result) + "for context=" + context);
		} else {
			if (!Types.isJavaType(context.getClassThatIsGenerated())) {
				logger.info("generated type = " + context.getClassThatIsGenerated().getSimpleName()
						+ "   value=" + getSafeObjectToString(result));
			}
		}
		return result;
	}

	private String getSafeObjectToString(final Object result) {
		if (result == null) return null;
		try {
			return result.toString();
		} catch (Exception e) {
			return "result.toString threw exception";
		}
	}


	/**
	 * checks if current GenerationSession is ready to be used - was properly begun and ended.
	 */
	public void assertReady() {
		if (currentState != State.STARTED) {
			if (currentState == State.UNDEFINED)
				throw new IllegalStateException("Session is not started. Did you forget to call ObjectG.setup()?");
			if (currentState == State.STOPED) {
				throw new IllegalStateException("Session is not stopped. Did you forget to call ObjectG.teardown()?");
			}
		}
	}

	/**
	 * call when GenerationSessions is about to begin
	 */
	public void begin() {
		logger.info("starting new GenerationSession from thread=" + Thread.currentThread());
		if (currentState == State.STARTED) {
			throw new IllegalStateException("You must stop session, before you can start new. " +
					"Did you forget to call ObjectG.teardown()?");
		}
		currentState = State.STARTED;
		sequenceFactory.resetManagedSequences();
		sessionStateLifeCycle.initManagedSessionState();
		setupGeneratorChain();
	}

	/**
	 * GenerationSession allows to store only SINGLE object (of any type)
	 *
	 * @param stateOwner object owning state
	 * @param state      state.
	 */
	void saveState(Object stateOwner, Object state) {
		managedState.put(stateOwner, state);
	}

	/**
	 * Return state relative to this GenerationSession
	 *
	 * @param stateOwner
	 * @param <T>
	 * @return
	 */
	<T> T getState(Object stateOwner) {
		return (T) managedState.get(stateOwner);
	}

	/**
	 * method for creating SessionState that can be used in {@link org.objectg.gen.Generator} or
	 * {@link org.objectg.gen.GenerationRule} to maintain their state. See {@link SessionState} for more info.
	 *
	 * @param stateOwner       not null object that is managing state
	 * @param valueClass       optional type of the value
	 * @param stateDescription not null description of the state
	 * @param <T>              type of the value that is managed. Only one type can be managed by one object.
	 * @return thread-safe SessionState that can be used in owner object.
	 */
	public static <T> SessionState<T> createManagedState(Object stateOwner, Class<T> valueClass,
			SessionStateDescription<T> stateDescription) {
		return sessionStateLifeCycle.createManagedState(stateOwner, stateDescription);
	}

	/**
	 * method for creating SessionState that can be used in {@link org.objectg.gen.Generator} or
	 * {@link org.objectg.gen.GenerationRule} to maintain their state. See {@link SessionState} for more info.
	 *
	 * @param stateOwner       not null object that is managing state
	 * @param stateDescription not null description of the state
	 * @param <T>              type of the value that is managed. Only one type can be managed by one object.
	 * @return thread-safe SessionState that can be used in owner object.
	 */
	public static <T> SessionState<T> createManagedState(Object stateOwner,
			SessionStateDescription<T> stateDescription) {
		return createManagedState(stateOwner, null, stateDescription);
	}

	private void setupGeneratorChain() {
		generatorChain = new CompositeGenerator(
				Generators.primitive(),
				Generators.list(),
				Generators.map(),
				Generators.set(),
				Generators.enumeration(),
				Generators.array(),
				Generators.notNativeClass());
	}

	/**
	 * call when GenerationSession is ended.
	 */
	public void end() {
		logger.info("stopping GenerationSession from thread=" + Thread.currentThread());
		if (currentState == State.STOPED) {
			throw new IllegalStateException("session was already stopped. Did you forget to call ObjectG.setup()?");
		}
		if (currentState == State.UNDEFINED) {
			throw new IllegalStateException("session was no started. Did you forget to call ObjectG.setup()?");
		}
		currentState = State.STOPED;
	}

	public static GenerationSession get() {
		if (threadInstance.get() == null) threadInstance.set(new GenerationSession());
		return threadInstance.get();
	}

	public SessionSequenceFactory getSequenceFactory() {
		return sequenceFactory;
	}

	private static enum State {
		UNDEFINED, STARTED, STOPED
	}
}