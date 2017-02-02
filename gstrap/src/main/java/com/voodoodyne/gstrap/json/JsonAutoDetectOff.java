package com.voodoodyne.gstrap.json;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A jackson mix-in annotation that disables autodetection.  Use @JsonProperty to add fields.
 */
@JsonAutoDetect(fieldVisibility= Visibility.NONE, getterVisibility= Visibility.NONE, isGetterVisibility= Visibility.NONE)
@JacksonAnnotationsInside
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface JsonAutoDetectOff {}
