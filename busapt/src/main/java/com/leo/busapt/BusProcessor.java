package com.leo.busapt;



import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

//@AutoService(Processor.class)
@SupportedAnnotationTypes(value = "com.leo.bus.Receive")
public class BusProcessor extends AbstractProcessor {

    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {

        super.init(processingEnvironment);

        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
        String handBusIndex = processingEnvironment.getOptions().get("handBusIndex");
        log(handBusIndex);
    }


    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        for (TypeElement typeElement : set) {
            Set<? extends Element> eventHandlers = roundEnvironment.getElementsAnnotatedWith(typeElement);
            for (Element eventHandler : eventHandlers) {
                ExecutableElement type = (ExecutableElement) eventHandler;
                String methodName = type.getSimpleName().toString();
                String clazzName = type.getEnclosingElement().toString();
                List<? extends VariableElement> parameters = type.getParameters();
                log(parameters.get(0).toString());
                for (VariableElement parameter : parameters) {

                TypeMirror typeMirror = parameter.asType();
                    log(typeMirror.getClass().toString());
                }

            }
            log(eventHandlers.toString());
        }
        return true;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
    private void log(String msg){
        System.err.println("BUS-APT : "+ msg);
    }
}