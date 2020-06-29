package user11681.mirror;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import sun.reflect.ConstructorAccessor;

@SuppressWarnings({"unchecked"})
public class Constructors {
    private static final Map<Class<?>, Set<Field>> ADDITIONS = new HashMap<>();

    /**
     * construct a new enum instance and add it to the class represented by {@code T}.
     *
     * @param values               the T[] returned by {@code T#values()}.
     * @param name                 the internal name of the new enum that will be used in {@code T#valueOf(String)} and {@code Enum#valueOf(Class, String)} and in {@code T#name()}.
     * @param constructorArguments the arguments of the desired constructor declared in {@code T}.
     * @param <T>                  an enum.
     * @return a new instance of {@code T}.
     */
    public static <T extends Enum<T>> T addEnumInstance(final T[] values, final String name, final Object... constructorArguments) {
        return addEnumInstance(newEnumInstance(values, name, constructorArguments));
    }

    /**
     * add {@code instance} to the internal fields of the class represented by {@code T}.
     *
     * @param instance an enum instance.
     * @param <T>      the enum to which {@code instance} belongs.
     * @return {@code instance}.
     */
    public static <T extends Enum<T>> T addEnumInstance(final T instance) {
        final Class<?> clazz = instance.getClass();
        final Field field = Fields.getEnumArrayField(clazz);

        try {
            final Field modifierField = Field.class.getDeclaredField("modifiers");
            final int modifiers = field.getModifiers();

            field.setAccessible(true);
            modifierField.setAccessible(true);
            modifierField.setInt(field, modifiers & ~Modifier.FINAL);

            final T[] original = (T[]) field.get(null);
            final int length = original.length;
            final T[] newValues = Arrays.copyOf(original, length + 1);

            newValues[length] = instance;
            field.set(null, newValues);

            final Field enumConstants = Class.class.getDeclaredField("enumConstants");
            final Field enumConstantDirectory = Class.class.getDeclaredField("enumConstantDirectory");

            enumConstants.setAccessible(true);
            enumConstants.set(clazz, null);
            enumConstantDirectory.setAccessible(true);
            enumConstantDirectory.set(clazz, null);

//            final Set<Field> additions;
//
//            if (ADDITIONS.containsKey(clazz)) {
//                additions = ADDITIONS.get(clazz);
//            } else {
//                additions = new ArraySet<>();
//                ADDITIONS.put(clazz, additions);
//
//                //noinspection ClassGetClass
//                addFields(clazz.getClass());
//            }
//
            Fields.addEnumField(instance);

            return instance;
        } catch (final IllegalAccessException | NoSuchFieldException exception) {
            throw new ReflectionException(exception);
        }
    }

    /**
     * construct a new enum instance of type {@code T}.
     *
     * @param values               the T[] returned by {@code T#values()}.
     * @param name                 the internal name of the new enum used by {@link Enum#name}.
     * @param constructorArguments the arguments of the desired constructor declared in {@code T}.
     * @param <T>                  an enum.
     * @return a new instance of {@code T}.
     */
    public static <T extends Enum<T>> T newEnumInstance(final T[] values, final String name, final Object... constructorArguments) {
        final int length = constructorArguments.length;
        final int originalLength = values.length;
        final Object[] enumArguments = new Object[length + 2];

        enumArguments[0] = name;
        enumArguments[1] = originalLength;

        System.arraycopy(constructorArguments, 0, enumArguments, 2, length);

        return (T) newInstance(values.getClass().getComponentType(), enumArguments);
    }

    /**
     * construct a new object of {@code T} via a constructor to which {@code arguments} can be passed.
     *
     * @param clazz     the {@link Class} object in which to search for constructors.
     * @param arguments the arguments corresponding to the parameters of a constructor in {@code clazz}.
     * @param <T>       the type of {@code clazz}.
     * @return the new instance.
     */
    public static <T> T newInstance(final Class<T> clazz, final Object... arguments) {
        for (final Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            final T instance = newInstance(constructor, arguments);

            if (instance != null) {
                return instance;
            }
        }

        throw new IllegalArgumentException(String.format("%s constructor with parameters of types %s was not found.", clazz.getName(),
                Arrays.toString(Arrays.stream(arguments).map(Object::getClass).toArray())
        ));
    }

    /**
     * construct a new instance of {@code T} via the given constructor and the given arguments.
     *
     * @param constructor the constructor to use in to construct an instance of {@code T}.
     * @param arguments   the arguments to pass to {@code constructor}.
     * @param <T>         a convenience type parameter to which to attempt to cast the new instance.
     * @return a new instance of {@code T}.
     */
    public static <T> T newInstance(final Constructor<?> constructor, final Object... arguments) {
        constructor.setAccessible(true);

        try {
            final Field constructorAccessor = Constructor.class.getDeclaredField("constructorAccessor");

            constructorAccessor.setAccessible(true);

            final Object accessor = constructorAccessor.get(constructor);

            if (accessor == null) {
                final Method acquireConstructorAccessor = Constructor.class.getDeclaredMethod("acquireConstructorAccessor");

                acquireConstructorAccessor.setAccessible(true);

                return (T) ((ConstructorAccessor) acquireConstructorAccessor.invoke(constructor)).newInstance(arguments);
            }

            return (T) (((ConstructorAccessor) accessor).newInstance(arguments));
        } catch (final IllegalArgumentException | InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException | NoSuchFieldException exception) {
            throw new ReflectionException(exception);
        }
    }

    /**
     * modify {@code clazz#getDeclaredFields0} to include the enum `{@linkplain Field fields} specified in ADDITIONS in the array returned thereby.
     *
     * @param clazz the {@linkplain Class class} to which to add the enum {@linkplain Field fields}.
     *//*

    private static void addFields(final Class<?> clazz) {
        final ClassNode classNode = new ClassNode();

        try {
            final ClassReader classReader = new ClassReader(clazz.getName());

            classReader.accept(classNode, 0);

            for (final MethodNode method : classNode.methods) {
                if (method.name.equals("getDeclaredFields0")) {
                    final InsnList insertion = method.instructions;

                    insertion.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    insertion.add(new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            "user11681/usersmanual/reflect/Constructors",
                            "appendFields",
                            "(Ljava/lang/Object;[Ljava/lang/reflect/Field;)[Ljava/lang/reflect/Field;",
                            false
                    ));
                    insertion.add(new VarInsnNode(Opcodes.ALOAD, 1));

                    final ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);

                    classNode.accept(classWriter);
                }
            }
        } catch (final IOException exception) {
            Main.LOGGER.error(String.format("attempt to read %s failed.", clazz.getName()), exception);
        }
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    public static Field[] appendFields(final Object clazz, final Field[] fields) {
        final Set<Field> addition = ADDITIONS.get(clazz);
        final int size = addition.size();
        final int length = fields.length;
        final Field[] newFields = Arrays.copyOf(fields, length + size);

        return addition.toArray(newFields);
    }
*/
    public static <C> Constructor<C> getConstructor(final Class<C> clazz, final Class<?>... parameterTypes) {
        try {
            return clazz.getDeclaredConstructor(parameterTypes);
        } catch (final NoSuchMethodException exception) {
            throw new ReflectionException(exception);
        }
    }
}
