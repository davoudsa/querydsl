package com.mysema.query.domain;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QueryInit;
import com.mysema.query.annotations.QuerySupertype;
import com.mysema.query.types.path.PathInits;
import com.mysema.query.types.path.PathMetadata;

public class EntityTest extends AbstractTest{
    
    private static final QEntity3 entity3 = QEntity3.entity3;
    
    @QueryEntity
    public static class EntityNoReferences {
        
    }
    
    @QueryEntity
    public static class Entity1 {
        public String entity1Field;
        
        public Entity1 entity1Ref;
    }
    
    @QueryEntity
    public static class Entity2 extends Supertype{        
        public String entity2Field;
        
        public Entity2 entity2Ref;
    }
    
    @QueryEntity
    public static class Entity3 extends Entity2{
        public String entity3Field;
        
        @QueryInit("*")
        public Entity3 entity3Ref;
    }
    
    @QueryEntity
    public static class Entity4 extends Supertype2{

    }
    
    @QuerySupertype
    public static class Supertype {
        public String supertypeField;        
        
        @QueryInit("entity2Ref")
        public Entity2 superTypeEntityRef;
    }
    
    @QuerySupertype
    public static class Supertype2 extends Supertype{

    }

    @Test
    public void testInheritance(){
        assertNotNull(entity3.entity3Ref.entity2Ref);
        assertNotNull(entity3.entity3Ref.entity3Ref);
        
        // super
        assertNotNull(entity3.entity3Ref._super.entity2Ref);        
    }
    
    @Test
    public void testSupertypePaths(){
        assertNotNull(entity3.superTypeEntityRef.entity2Ref);
        assertNotNull(entity3._super.superTypeEntityRef.entity2Ref);
        assertNotNull(entity3._super._super.superTypeEntityRef.entity2Ref);
        
        assertNotNull(QEntity4.entity4.supertypeField);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testConstructors() throws SecurityException, NoSuchMethodException{
        Class[] types = new Class[]{Class.class, String.class, PathMetadata.class, PathInits.class};        
        QEntity1.class.getConstructor(types);
        QEntity2.class.getConstructor(types);
        QEntity3.class.getConstructor(types);
        QEntity4.class.getConstructor(types);
        QSupertype.class.getConstructor(types);
        QSupertype2.class.getConstructor(types);
    }
    
    @SuppressWarnings("unchecked")
    @Test(expected=NoSuchMethodException.class)
    public void testConstructors2() throws SecurityException, NoSuchMethodException{
        Class[] types = new Class[]{Class.class, String.class, PathMetadata.class, PathInits.class};
        QEntityNoReferences.class.getConstructor(types);
    }
    
}
