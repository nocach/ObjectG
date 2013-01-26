package org.objectg.gen.session;

import org.springframework.util.Assert;

/**
 * <p>
 *     SessionState is the interface through which user should manage states in GenerationSession.
 * </p>
 * <p>
 *     ANY state that is changing during generation and thus can affect on generation result MUST be managed
 *     through this class. Use {@link GenerationSession#createManagedState(Object, Class, SessionStateDescription)}
 * </p>
 * <p>
 *     <b>Why</b>: You want to isolate your tests. If some rule is used globally (e.g. configured in BeforeClass setup)
 *     then it will have global state. Global state will be dependent on how many tests were run. So the result
 *     of generating values is dependent on other tests. You will get different results when launching
 *     ONLY testA compared to launching TestCase with tests testB,testA,testC.
 *     <p>
 *         Also SessionState stores state per Thread, so multithread launching of tests become possible.
 *     </p>
 * </p>
 * <p>
 *     <b>How:</b> SessionState manages state between multiple Generations (multiple generation sessions).
 *     When you start new test execution you want to start from INITIAL defined state. This INITIAL state is a value
 *     returned in {@link SessionStateDescription#getInitValue()}.
 *     During execution you save your state linked to current GenerationSession (per Thread basis) and that is why
 *     concurrent tests will have no effect on how objects are generated during test execution.
 * </p>
 * <p>
 * User: __nocach
 * Date: 26.1.13
 * </p>
 */
public final class SessionState<T> {
	private Object stateOwner;
	private SessionStateDescription<T> stateDescription;

	/**
	 *
	 * @param stateOwner not null
	 * @param stateDescription not null
	 */
	SessionState(Object stateOwner, SessionStateDescription<T> stateDescription){
		Assert.notNull(stateDescription, "stateDescription should not be null");
		Assert.notNull(stateOwner, "stateOwner should not be null");
		this.stateOwner = stateOwner;
		this.stateDescription = stateDescription;
	}
	public void set(T newValue){
		GenerationSession.get().saveState(stateOwner, newValue);
	}
	public T get(){
		return (T)GenerationSession.get().getState(stateOwner);
	}
	void init(){
		GenerationSession.get().saveState(stateOwner, stateDescription.getInitValue());
	}
}
