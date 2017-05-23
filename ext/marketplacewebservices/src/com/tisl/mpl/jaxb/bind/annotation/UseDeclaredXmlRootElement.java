package com.tisl.mpl.jaxb.bind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  Custom Annotation to be used in WSDTO classes.
 *  Currently there is a logic in "WsDTOGenericMetadataSourceAdapter", because
 *  of which, logic is hardcoded for JAXB XMLRootElement mapping.
 *  This has impacted the standard use of @XmlRootElement in the WsDTO.
 *  This custom annotation will help to override that logic.
 *  If this annotation is used in any DTO class then the name of
 *  root element as used in @XmlRootElement will be used.
 *  This will be used only if, @UseDeclaredXmlRootElement(enabled=true)
 *  annotation is used in any WsDTO class.
 *  In case this annotation is missing, there will not be any
 *  impact to existing WsDTOs.
 *  Added during R2.3
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface UseDeclaredXmlRootElement {

        boolean enabled() default false;

}
