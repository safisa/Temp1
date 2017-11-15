package com.k2view.cdbms.usershared;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.Repeatable;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

import com.fasterxml.jackson.databind.SerializationFeature;

import org.codehaus.jackson.annotate.JsonAutoDetect;


/**
 * 
 * Get a Reflection structure of a Class.
 * 
 * This is mainly used by the Studio to get Metadata about user code by running main and obtaining
 * the json representation of the class.
 * 
 * To get parameter names, the target class must be compiled with -parameters
 * 
 * @author yuvalp
 *
 */
@SuppressWarnings("unused")
public class UserCodeDescribe {

//	@Target(METHOD)
//	@Retention(RUNTIME)
//	public @interface userFunction {
//	}

	@Retention(RUNTIME)
	@Target({ METHOD, FIELD })
	public @interface category {
		String value();
	}

	@Retention(RUNTIME)
	@Target({ TYPE, METHOD, PARAMETER, FIELD })
	public @interface desc {
		String value();
	}
	
	
	public static enum FunctionType {
		RegularFunction, RootFunction, LudbFunction, DecisionFunction, WebService, SwitchFunction
		//regular, root, ludb, decision, webservice, switchFunction
	}

	@Retention(RUNTIME)
	@Target({METHOD})
	public @interface type {
		FunctionType value();
	}
	
	@Retention(RUNTIME)
	@Target({METHOD})
	@Repeatable(outs.class)
	public @interface out {
		String name();
		@SuppressWarnings("rawtypes")
		String type();
		//@SuppressWarnings("rawtypes")
		//Class type();
		String desc() default "";
	}
	
	@Retention(RUNTIME)
	@Target({METHOD})
	public @interface outs {
		out [] value();
	}

	
	private static Class<?> [] propertyAnnotations = {category.class, desc.class, type.class, outs.class};

	private class M {
		final public String name;
		final public List<F> parameters = new ArrayList<>();
		final public String returnValue;
		final public List<String> modifiers = new ArrayList<>();
		final public Map<String,Object> properties = new HashMap<>();
		
		M(Method method) {
			name = method.getName();
			returnValue = string(method.getGenericReturnType());
			for (Parameter p:method.getParameters()) {
				parameters.add(new F(p));
			}
			modifiers.addAll(Arrays.asList(Modifier.toString(method.getModifiers()).split(" ")));
			annotations(method, properties, errors);
		}
	}
	
	public static class F {
		private F(Parameter p) {
			this(p.getName(), p.getParameterizedType(), p.getAnnotation(desc.class));
		}
		private F(Field f) {
			this(f.getName(), f.getGenericType(), f.getAnnotation(desc.class));
		}
		private F(String name, Type type, desc desc) {
			this.name = name;
			this.type = string(type);
			this.desc = desc==null?null:desc.value();
		}
		
		final public String name;
		final public String type;
		final public String desc;
	}
	
	final public String name;
	final public List<String> errors = new ArrayList<>();
	final public List<M> methods = new ArrayList<>();
	final public List<F> fields = new ArrayList<>();
	final public Map<String,Object> properties = new HashMap<>();
	//final public List<UserCodeDescribe> innerClasses = new ArrayList<>();

    public UserCodeDescribe(String param) {
    	name = init(param);
    }
    
	private String init(String param) {
        try {
            Class<?> c = UserCodeDescribe.class.getClassLoader().loadClass(param);
            return init(c);
        } catch (ClassNotFoundException e) {
            errors.add(e.toString());
            return param;
        }
	}

    private String init(Class<?> c) {
        for (Method m:c.getDeclaredMethods()) {
        	methods.add(new M(m));
        }
        annotations(c, properties, errors);
        //for (Class<?> ic:c.getDeclaredClasses()) {
        //	innerClasses.add(new UserCodeDescribe(ic));
        //}
        for (Field f:c.getDeclaredFields()) {
        	fields.add(new F(f));
        }
        return c.getCanonicalName();
	}

	public UserCodeDescribe(Class<?> c) {
    	name = init(c);
    }

    @SuppressWarnings("unchecked")
	private static void annotations(AnnotatedElement annotated, Map<String, Object> properties, List<String> errors) {
    	try {
	    	annotated.getAnnotationsByType(category.class);
	    	for (Class<?> pa:propertyAnnotations) {
	    		Annotation[] aa = annotated.getAnnotationsByType((Class<? extends Annotation>)pa);
	    		for (Annotation a:aa) {
	    			Class <?>type = a.annotationType();
	    			Method m = type.getMethod("value");
	    			Object o = "";
	    			if (m!=null) {
	    				o = m.invoke(a);
	    				if (o instanceof out[]) {
	    					o = decode((out[])o);
	    				} else if (o!=null) {
    						o = o.toString();
	    				}
	    			}
	    			properties.put(type.getSimpleName(), o); 
	    					
	    		}
	    	} 
    	} catch (Throwable e) {
    		errors.add(e.getMessage());
		}
	}

	private static Object decode(out [] oo) {
		List<Map<String,String>> l= new ArrayList<>();
		for (out o:oo) {
			Map<String,String> m = new HashMap<>();
			m.put("name",o.name());
			m.put("type", o.type().toString());
			m.put("desc",  o.desc());
			l.add(m);
		}
		return l;
	}

	private static String string(Type type) {
		return type.getTypeName();
	}

	public static void main(String [] params) throws Throwable {
        List<UserCodeDescribe> r = new ArrayList<>();
        for (String param:params) {
            r.add(new UserCodeDescribe(param));
         }
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibilityChecker(mapper.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE)
    		);
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(r));

    }


}
