package com.github.jmsilla.newspapermethodsort.handlers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

class MethodVisitor extends ASTVisitor {
    private List<FieldDeclaration> publicFields = new ArrayList<>();
    private List<FieldDeclaration> nonPublicFields = new ArrayList<>();
    private List<MethodDeclaration> staticMethods = new ArrayList<>();
    private List<MethodDeclaration> instanceMethods = new ArrayList<>();

    @Override
    public boolean visit(MethodDeclaration node) {
        if (Modifier.isStatic(node.getModifiers()))
            staticMethods.add(node);
        else
            instanceMethods.add(node);
        
        return super.visit(node);
    }
    
    @Override
    public boolean visit(FieldDeclaration node) {
        if (Modifier.isPublic(node.getModifiers()))
            publicFields.add(node);
        else
            nonPublicFields.add(node);
        
        return super.visit(node);
    }

    public List<FieldDeclaration> getPublicFields() {
        return publicFields;
    }

    public List<FieldDeclaration> getNonPublicFields() {
        return nonPublicFields;
    }

    public List<MethodDeclaration> getStaticMethods() {
        return staticMethods;
    }

    public List<MethodDeclaration> getInstanceMethods() {
        return instanceMethods;
    }
}

public class NewspaperSortHandler extends AbstractHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
	    IEditorPart activeEditor = window.getActivePage().getActiveEditor();
        IJavaElement javaElement = JavaUI.getEditorInputJavaElement(activeEditor.getEditorInput());
        
        if (javaElement instanceof ICompilationUnit) {
            ICompilationUnit unit = (ICompilationUnit) javaElement;
            
            ASTParser parser = ASTParser.newParser(AST.JLS11);
            parser.setKind(ASTParser.K_COMPILATION_UNIT);
            parser.setSource(unit);
            parser.setResolveBindings(true);
            
            CompilationUnit cunit = (CompilationUnit) parser.createAST(null);
            System.out.println("OK: " + cunit);
            
            MethodVisitor methodVisitor = new MethodVisitor();
            cunit.accept(methodVisitor);
            
            System.out.println("Private fields: ");
            methodVisitor.getNonPublicFields().forEach(f -> System.out.println(f.fragments().get(0)));
            
            System.out.println("Instance method list: ");
            methodVisitor.getInstanceMethods().forEach(m -> System.out.println(m.getName()));
        }
        
		return null;
	}
}
