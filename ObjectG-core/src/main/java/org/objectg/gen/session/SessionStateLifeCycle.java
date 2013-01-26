package org.objectg.gen.session;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.WeakHashMap;

import org.springframework.util.Assert;

/**
 * <p>
 *     Class for managing life cycle of states between GenerationSessions
 * </p>
 * <p>
 *     <b>Why</b>: object that knows how to manage lifecycle of states in GenerationSession. Prevents
 *     memory leaks using WeakReferences.
 * </p>
 * <p>
 *     <b>How</b>: SessionState is an object that is created once and after it is created state managed by it is
 *     stored and retrieved relevant to GenerationSession. Starting of new GenerationSession requires that this
 *     state is initialized (see {@link SessionStateDescription}. This class provides methods on how to init
 *     those states when new generation session is started.
 * </p>
 * <p>
 * User: __nocach
 * Date: 26.1.13
 * </p>
 */
class SessionStateLifeCycle {
	private Set<WeakReference<SessionState>> managedSessionState = Collections.synchronizedSet(new HashSet<WeakReference<SessionState>>());

	public <T> SessionState<T> createManagedState(Object stateOwner, SessionStateDescription<T> stateDescription){
		final SessionState<T> result = new SessionState<T>(stateOwner, stateDescription);
		result.init();
		managedSessionState.add(new WeakReference<SessionState>(result));
		return result;
	}

	/**
	 * initiates all currently present SessionState
	 */
	void initManagedSessionState() {
		final Iterator<WeakReference<SessionState>> iterator = managedSessionState.iterator();
		synchronized(iterator){
			while(iterator.hasNext()){
				final WeakReference<SessionState> stateRef = iterator.next();
				final SessionState state = stateRef.get();
				if (state != null) {
					state.init();
				}
				else {
					iterator.remove();
				}
			}
		}
	}
}
