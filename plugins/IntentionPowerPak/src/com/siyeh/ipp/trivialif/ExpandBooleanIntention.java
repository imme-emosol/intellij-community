package com.siyeh.ipp.trivialif;

import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.siyeh.ipp.base.Intention;
import com.siyeh.ipp.base.PsiElementPredicate;
import org.jetbrains.annotations.NotNull;

public class ExpandBooleanIntention extends Intention{
    public String getText(){
        return "Expand boolean use to if-else";
    }

    public String getFamilyName(){
        return "Expand Boolean";
    }

    @NotNull
    public PsiElementPredicate getElementPredicate(){
        return new ExpandBooleanPredicate();
    }

    public void processIntention(PsiElement element)
            throws IncorrectOperationException{
        final PsiJavaToken token = (PsiJavaToken) element;
        final PsiStatement containingStatement =
                PsiTreeUtil.getParentOfType(token, PsiStatement.class);
        assert containingStatement != null;
        if(ExpandBooleanPredicate.isBooleanAssignment(containingStatement)){
            final PsiExpressionStatement assignmentStatement =
                    (PsiExpressionStatement) containingStatement;
            final PsiAssignmentExpression assignmentExpression =
                    (PsiAssignmentExpression) assignmentStatement.getExpression();
            final PsiExpression rhs = assignmentExpression.getRExpression();
            assert rhs != null;
            final String rhsText = rhs.getText();
            final PsiExpression lhs = assignmentExpression.getLExpression();
            final String lhsText = lhs.getText();
            final String statement =
                    "if(" + rhsText + "){" + lhsText + " = true;}else{" +
                            lhsText +
                            " = false;}";
            replaceStatement(statement, containingStatement);
        } else if(ExpandBooleanPredicate.isBooleanReturn(containingStatement)){
            final PsiReturnStatement returnStatement =
                    (PsiReturnStatement) containingStatement;
            final PsiExpression returnValue = returnStatement.getReturnValue();
            final String valueText = returnValue.getText();
            final String statement =
                    "if(" + valueText + "){return true;}else{return false;}";
            replaceStatement(statement, containingStatement);
        }
    }
}