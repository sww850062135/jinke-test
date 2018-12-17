package com.sww.jinke.test.entity;


import lombok.Getter;
import lombok.Setter;

/**
 * 单元关联实体
 */
@Setter
@Getter
public class Unit_correlations {
    private String id;
    private String jkcommunityId;
    private String jkbuildunitId;
    private String ajbbuildunitId;
    private String ajbbuildunitCode;
}
