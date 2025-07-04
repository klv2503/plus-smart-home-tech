package ru.yandex.practicum.exceptions.response;

import lombok.Getter;

@Getter
public class StackTraceElementDto {
    private final String classLoaderName;
    private final String moduleName;
    private final String moduleVersion;
    private final String methodName;
    private final String fileName;
    private final int lineNumber;
    private final String className;
    private final boolean nativeMethod;

    public StackTraceElementDto(StackTraceElement e) {
        this.classLoaderName = null; // не извлекается напрямую
        this.moduleName = e.getModuleName();
        this.moduleVersion = e.getModuleVersion();
        this.methodName = e.getMethodName();
        this.fileName = e.getFileName();
        this.lineNumber = e.getLineNumber();
        this.className = e.getClassName();
        this.nativeMethod = e.isNativeMethod();
    }
}
