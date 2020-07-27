package com.leo.busapt;

import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.WildcardTypeName;

/**
 * <p>Date:2020/7/27.2:13 PM</p>
 * <p>Author:leo</p>
 * <p>Desc: apt使用到的TypeName
 * </p>
 */
public final class Consts {
    public static final String APP_PACKAGE = "com.leo.bus";
    public static final ClassName com_leo_bus_EventMethodInfo = ClassName.get(APP_PACKAGE, "EventMethodInfo");
    public static final ClassName com_leo_bus_EventIndexInfoWrap = ClassName.get(APP_PACKAGE, "EventIndexInfoWrap");
    public final static ClassName com_leo_bus_EventIndexInfo = ClassName.get(APP_PACKAGE, "EventIndexInfo");
    public static final ArrayTypeName com_leo_bus_EventIndexInfo_array = ArrayTypeName.of(com_leo_bus_EventIndexInfo);


    public static final ClassName com_leo_bus_IndexFinder = ClassName.get(APP_PACKAGE, "IndexFinder");

    //Class<?>
    private static TypeName wildcard = WildcardTypeName.subtypeOf(Object.class);
    private static ClassName cls = ClassName.get(Class.class);
    public static TypeName clazzType = ParameterizedTypeName.get(cls, wildcard);
}
