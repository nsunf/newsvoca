package com.nsunf.newsvoca.entity.composite_key;

import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode
public class ParagraphId implements Serializable {
    private Long id;
    private Long contentOrder;
}
