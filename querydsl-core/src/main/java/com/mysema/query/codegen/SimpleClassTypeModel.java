/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.List;

import net.jcip.annotations.Immutable;

import org.apache.commons.lang.ClassUtils;

import com.mysema.commons.lang.Assert;

/**
 * @author tiwe
 *
 */
@Immutable
public class SimpleClassTypeModel implements TypeModel{
    
    private final Class<?> clazz;
    
    private final List<TypeModel> parameters;
    
    private final Class<?> primitiveClass;
    
    private final TypeCategory typeCategory;
    
    private final boolean visible;
    
    public SimpleClassTypeModel(TypeCategory typeCategory, Class<?> clazz){
        this(typeCategory, clazz, ClassUtils.wrapperToPrimitive(clazz));
    }
    
    public SimpleClassTypeModel(TypeCategory typeCategory, Class<?> clazz, Class<?> primitiveClass){
        this.typeCategory = Assert.notNull(typeCategory);
        this.clazz = Assert.notNull(clazz);
        this.primitiveClass = primitiveClass;
        this.parameters = Collections.emptyList();
        this.visible = clazz.getPackage().getName().equals("java.lang");
    }
    
    @Override
    public TypeModel as(TypeCategory category) {
        if (typeCategory == category){
            return this;
        }else{
            return new SimpleClassTypeModel(category, clazz);
        }
    }

    @Override
    public String getFullName() {
        return clazz.getName();
    }

    @Override
    public StringBuilder getLocalGenericName(TypeModel context, StringBuilder builder, boolean asArgType) {
        return getLocalRawName(context, builder);
    }

    @Override
    public StringBuilder getLocalRawName(TypeModel context, StringBuilder builder) {
        if (visible || context.getPackageName().equals(clazz.getPackage().getName())){
            builder.append(clazz.getName().substring(clazz.getPackage().getName().length()+1));    
        }else{
            builder.append(clazz.getName());
        }        
        return builder;
    }

    @Override
    public String getPackageName() {
        return clazz.getPackage().getName();
    }

    @Override
    public TypeModel getParameter(int i) {
        return parameters.get(i);
    }

    @Override
    public int getParameterCount() {
        return parameters.size();
    }

    @Override
    public String getPrimitiveName() {
        return primitiveClass != null ? primitiveClass.getSimpleName() : null;
    }

    @Override
    public TypeModel getSelfOrValueType() {
        if (typeCategory.isSubCategoryOf(TypeCategory.COLLECTION) 
         || typeCategory.isSubCategoryOf(TypeCategory.MAP)){
            return parameters.get(parameters.size()-1);
        }else{
            return this;
        }
    }

    @Override
    public String getSimpleName() {
        return clazz.getSimpleName();
    }

    @Override
    public TypeCategory getTypeCategory() {
        return typeCategory;
    }

    @Override
    public boolean isPrimitive() {
        return primitiveClass != null;
    }
    
    @Override
    public boolean equals(Object o){
        if (o instanceof TypeModel){
            TypeModel t = (TypeModel)o;
            return clazz.getName().equals(t.getFullName());
        }else{
            return false;
        }
    }

    public int hashCode(){
        return clazz.getName().hashCode();
    }

    @Override
    public boolean isFinal() {
        return Modifier.isFinal(clazz.getModifiers());
    }

    @Override
    public boolean hasEntityFields() {
        return false;
    }

    @Override
    public String getQueryTypeName(EntityModel context) {
        return null;
    }
}
