package cz.nocach.masaryk.objectg.gen;

/**
 * <p>
 *     Interface used to generate values for specific types.
 * </p>
 * <p>
 * User: __nocach
 * Date: 26.8.12
 * </p>
 */
public interface Generator {
    /**
     * generate new value for the passed type
     *
     * @param type type of which value to generate
     * @return new value
     */
    public <T> T generate(Class<T> type);

    /**
     *
     * @param type
     * @return true if this generator can generate value for the passed type,
     *          false otherwise
     */
    public boolean supportsType(Class type);
}
