package com.github.jmsilla.newspapermethodsort.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

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
        }
        
		return null;
	}
}
