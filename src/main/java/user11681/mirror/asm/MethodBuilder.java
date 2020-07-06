package user11681.mirror.asm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.objectweb.asm.Type;

public class MethodBuilder extends MemberBuilder<MethodBuilder> {
    protected final TypeBuilder builder;
    protected final Map<String, Class<?>> parameters;
    protected final Set<String> exceptions;

    protected String returnType;

    public MethodBuilder(final TypeBuilder builder) {
        this.builder = builder;
        this.parameters = new HashMap<>();
        this.exceptions = new HashSet<>();
    }

    public MethodBuilder returns(final Class<?> returnType) {
        return this.returns(Type.getDescriptor(returnType));
    }

    public MethodBuilder returns(final String returnType) {
        this.returnType = returnType;

        return this;
    }

    public MethodBuilder parameter(final Class<?> clazz, final String name) {
        this.parameters.put(name, clazz);

        return this;
    }

    public MethodBuilder exception(final String exception) {
        this.exceptions.add(exception);

        return this;
    }

/*
    public MethodBuilder body() {
        return this;
    }
*/

    public TypeBuilder build() {
        return this.builder;
    }
}
