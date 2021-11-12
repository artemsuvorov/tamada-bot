package Commands;

import java.util.function.Predicate;

public class InputPredicate {

    private Predicate<String> inputFitsView;
    private PredicateComposition predicateComposition;
    private SubstringComposition substringComposition;
    private boolean isNegated;

    public InputPredicate() {
        this(PredicateComposition.Or, SubstringComposition.Any, false, null);
    }

    private InputPredicate(PredicateComposition predicateComposition,
        SubstringComposition substringComposition, boolean isNegated, Predicate<String> predicate) {
        this.predicateComposition = predicateComposition;
        this.substringComposition = substringComposition;
        this.isNegated = isNegated;
        this.inputFitsView = predicate;
    }

    public boolean match(String input) {
        if (inputFitsView == null)
            inputFitsView = (inp) -> true;
        return inputFitsView.test(input);
    }

    public InputPredicate or() {
        predicateComposition = PredicateComposition.Or;
        return compositePredicateWith();
    }

    public InputPredicate and() {
        predicateComposition = PredicateComposition.And;
        return compositePredicateWith();
    }

    public InputPredicate has(String substring) {
        return any(substring);
    }

    public InputPredicate any(String... substrings) {
        substringComposition = SubstringComposition.Any;
        return compositePredicateWith(substrings);
    }

    public InputPredicate all(String... substrings) {
        if (substrings == null || substrings.length <= 0)
            throw new IllegalArgumentException("The substrings to test cannot be null or empty.");
        substringComposition = SubstringComposition.All;
        return compositePredicateWith(substrings);
    }

    public InputPredicate not() {
        isNegated = !isNegated;
        return compositePredicateWith();
    }

    private InputPredicate compositePredicateWith(String... substrings) {
        if (predicateComposition == PredicateComposition.None)
            throw new IllegalArgumentException("Predicate composition (`or`/`and`) type is not specified.");
        if (substringComposition == SubstringComposition.None)
            throw new IllegalArgumentException("Substring composition (`any`/`all`) type is not specified.");
        if (inputFitsView == null)
            inputFitsView = getEmptyPredicate(predicateComposition);
        if (substrings == null || substrings.length <= 0)
            return this;
        var containsSubstrings = getContainsSubstringsPredicate(substringComposition, substrings);
        inputFitsView = getCombinedPredicate(predicateComposition, containsSubstrings);
        return new InputPredicate(predicateComposition, SubstringComposition.Any, isNegated, inputFitsView);
    }

    private Predicate<String> getEmptyPredicate(PredicateComposition predicateComposition) {
        if (predicateComposition == PredicateComposition.Or)
            return (input) -> false;
        else if (predicateComposition == PredicateComposition.And)
            return (input) -> true;
        else
            throw new IllegalArgumentException("Invalid predicate composition type.");
    }

    private Predicate<String> getContainsSubstringsPredicate
            (SubstringComposition substringComposition, String... substrings) {
        if (substrings == null || substrings.length <= 0)
            return getEmptyPredicate(predicateComposition);
        if (substringComposition == SubstringComposition.Any)
            return (input) -> inputContainsAny(input, substrings);
        else if (substringComposition == SubstringComposition.All)
            return  (input) -> inputContainsAll(input, substrings);
        else
            throw new IllegalArgumentException("Invalid substring composition type.");
    }

    private Predicate<String> getCombinedPredicate
            (PredicateComposition predicateComposition, Predicate<String> other) {
        Predicate<String> result;
        if (predicateComposition == PredicateComposition.Or)
            result = inputFitsView.or(other);
        else if (predicateComposition == PredicateComposition.And)
            result = inputFitsView.and(other);
        else
            throw new IllegalArgumentException("Invalid predicate composition type.");
        return isNegated ? result.negate() : result;
    }

    /**
     * Указывает, содержит ли указанная строка любую из перечисленных подстрок.
     * @param input  строка, в которой будет произведен поиск перечисленных подстрок.
     * @param values подстроки, которые будут искаться в указанной строке.
     * @return true, если строка содержит любую из перечисленных подстрок, иначе false.
     */
    private boolean inputContainsAny(String input, String... values) {
        var result = false;
        for (var value : values) {
            result = input.contains(value);
            if (result) break;
        }
        return result;
    }

    /**
     * Указывает, содержит ли указанная строка каждую из перечисленных подстрок.
     * @param input строка, в которой будет произведен поиск перечисленных подстрок.
     * @param values подстроки, которые будут искаться в указанной строке.
     * @return true, если строка содержит каждую из перечисленных подстрок, иначе false.
     */
    private boolean inputContainsAll(String input, String... values) {
        var result = true;
        for (var value : values) {
            result = input.contains(value);
            if (!result) break;
        }
        return result;
    }

}