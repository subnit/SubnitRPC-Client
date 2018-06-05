package com.subnit.rpc.util;

import lombok.Data;

/**
 * description:
 * date : create in 10:18 2018/6/5
 * modified by :
 *
 * @author subo
 */
@Data
public class MethodDTO {
    private String className;
    private String methodName;
    private String parameterTypesString;
    private String argsString;

    public MethodDTO() {
    }

    public MethodDTO(String className,
                     String methodName,
                     String parameterTypesString,
                     String argsString) {
        this.className = className;
        this.methodName = methodName;
        this.parameterTypesString = parameterTypesString;
        this.argsString = argsString;
    }

}
