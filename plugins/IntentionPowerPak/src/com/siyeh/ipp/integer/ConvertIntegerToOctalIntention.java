package com.siyeh.ipp.integer;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLiteralExpression;
import com.intellij.util.IncorrectOperationException;
import com.siyeh.ipp.base.Intention;
import com.siyeh.ipp.base.PsiElementPredicate;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

public class ConvertIntegerToOctalIntention extends Intention{
    public String getText(){
        return "Convert to octal";
    }

    public String getFamilyName(){
        return "Convert To Octal";
    }

    @NotNull
    public PsiElementPredicate getElementPredicate(){
        return new ConvertIntegerToOctalPredicate();
    }

    public void processIntention(PsiElement element)
            throws IncorrectOperationException{
        final PsiLiteralExpression exp = (PsiLiteralExpression) element;
        String textString = exp.getText();
        final int textLength = textString.length();
        final char lastChar = textString.charAt(textLength - 1);
        final boolean isLong = lastChar == 'l' || lastChar == 'L';
        if(isLong){
            textString = textString.substring(0, textLength - 1);
        }
        final BigInteger val;
        if(textString.startsWith("0x")){
            final String rawTextString = textString.substring(2);
            val = new BigInteger(rawTextString, 16);
        } else{
            val = new BigInteger(textString, 10);
        }
        String octString = '0' + val.toString(8);
        if(isLong){
            octString += 'L';
        }
        replaceExpression(octString, exp);
    }
}
