package com.siyeh.ipp.conditional;

import com.intellij.psi.PsiConditionalExpression;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpression;
import com.intellij.util.IncorrectOperationException;
import com.siyeh.ipp.base.Intention;
import com.siyeh.ipp.base.PsiElementPredicate;
import com.siyeh.ipp.psiutils.BoolUtils;
import org.jetbrains.annotations.NotNull;

public class FlipConditionalIntention extends Intention{
    public String getText(){
        return "Flip ?:";
    }

    public String getFamilyName(){
        return "Flip Conditional";
    }

    @NotNull
    public PsiElementPredicate getElementPredicate(){
        return new FlipConditionalPredicate();
    }

    public void processIntention(PsiElement element)
            throws IncorrectOperationException{
        final PsiConditionalExpression exp =
                (PsiConditionalExpression) element;

        final PsiExpression condition = exp.getCondition();
        final PsiExpression elseExpression = exp.getElseExpression();
        final PsiExpression thenExpression = exp.getThenExpression();
        assert elseExpression != null;
        assert thenExpression != null;
        final String newExpression =
                BoolUtils.getNegatedExpressionText(condition) + '?' +
                        elseExpression.getText() +
                        ':' +
                        thenExpression.getText();
        replaceExpression(newExpression, exp);
    }
}
