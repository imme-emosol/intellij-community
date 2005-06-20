package com.siyeh.ipp.trivialif;

import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.siyeh.ipp.base.Intention;
import com.siyeh.ipp.base.PsiElementPredicate;
import org.jetbrains.annotations.NotNull;

public class MergeParallelIfsIntention extends Intention{
    public String getText(){
        return "Merge 'if's";
    }

    public String getFamilyName(){
        return "Merge Parallel Ifs";
    }

    @NotNull
    public PsiElementPredicate getElementPredicate(){
        return new MergeParallelIfsPredicate();
    }

    public void processIntention(PsiElement element)
            throws IncorrectOperationException{
        final PsiJavaToken token = (PsiJavaToken) element;
        final PsiIfStatement firstStatement = (PsiIfStatement) token.getParent();
        final PsiIfStatement secondStatement =
                (PsiIfStatement) PsiTreeUtil.skipSiblingsForward(firstStatement,
                                                                 new Class[]{
                                                                     PsiWhiteSpace.class});

        final String statement = mergeIfStatements(firstStatement, secondStatement);

        replaceStatement(statement, firstStatement);
        assert secondStatement != null;
        secondStatement.delete();
    }

    private String mergeIfStatements(PsiIfStatement firstStatement,
                                     PsiIfStatement secondStatement){
        final PsiExpression condition = firstStatement.getCondition();
        final String conditionText = condition.getText();

        final PsiStatement firstThenBranch = firstStatement.getThenBranch();
        final PsiStatement secondThenBranch = secondStatement.getThenBranch();
        String statement =
                "if(" + conditionText + ')' +
                        printStatementsInSequence(firstThenBranch,
                                                  secondThenBranch);

        final PsiStatement firstElseBranch = firstStatement.getElseBranch();
        final PsiStatement secondElseBranch = secondStatement.getElseBranch();
        if(firstElseBranch != null || secondElseBranch != null){
            if(firstElseBranch instanceof PsiIfStatement
                    && secondElseBranch instanceof PsiIfStatement
                    &&
                    MergeParallelIfsPredicate
                            .ifStatementsCanBeMerged((PsiIfStatement) firstElseBranch,
                                                     (PsiIfStatement) secondElseBranch)){
                statement += "else " +
                        mergeIfStatements((PsiIfStatement) firstElseBranch,
                                          (PsiIfStatement) secondElseBranch);
            } else{
                statement += "else" +
                        printStatementsInSequence(firstElseBranch,
                                                  secondElseBranch);
            }
        }
        return statement;
    }

    private static String printStatementsInSequence(PsiStatement statement1,
                                                    PsiStatement statement2){
        if(statement1 == null){
            return ' ' + statement2.getText();
        }
        if(statement2 == null){
            return ' ' + statement1.getText();
        }
        final StringBuffer out = new StringBuffer();
        out.append('{');
        printStatementStripped(statement1, out);
        printStatementStripped(statement2, out);
        out.append('}');
        return out.toString();
    }

    private static void printStatementStripped(PsiStatement statement,
                                               StringBuffer out){
        if(statement instanceof PsiBlockStatement){
            final PsiCodeBlock block =
                    ((PsiBlockStatement) statement).getCodeBlock();
            final PsiElement[] children = block.getChildren();
            for(int i = 1; i < children.length - 1; i++){
                out.append(children[i].getText());
            }
        } else{
            out.append(statement.getText());
        }
    }
}