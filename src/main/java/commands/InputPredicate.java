package commands;

import java.util.Arrays;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Представляет собой обертку над предикатом от строки String,
 * который посредством реализации fluent interface позволяет
 * конструировать различные предикаты.
 */
public final class InputPredicate {

    private Predicate<String> inputFitsView;
    private PredicateComposition predicateComposition;
    private SubstringComposition substringComposition;
    private boolean isNegated = false;
    private boolean caseInsensitive = true;

    public InputPredicate() {
        this(PredicateComposition.Or, SubstringComposition.Any, false, true, null);
    }

    private InputPredicate(PredicateComposition predicateComposition, SubstringComposition substringComposition,
                           boolean isNegated, boolean caseInsensitive, Predicate<String> predicate) {
        this.predicateComposition = predicateComposition;
        this.substringComposition = substringComposition;
        this.isNegated = isNegated;
        this.caseInsensitive = caseInsensitive;
        this.inputFitsView = predicate;
    }

    /**
     * Проверяет, удовлетворяет ли указанная строка этому предикату.
     * Например, предикату new InputPredicate().has("при").and().has("вет")
     * удовлетворяют строки "ПРИВЕТ", "при-вет" и не удовлетворяет "пр-вет".
     * По умолчанию, все предикаты не учитывают регистр строк.
     * Считается, что пустому предикату удовлетворяет любая строка.
     * @param input строка, которая будет проверена на соответствие предикату.
     * @return true, если указанная строка удовлетворяет этому предикату,
     * иначе false.
     */
    public boolean match(String input) {
        if (inputFitsView == null)
            inputFitsView = (inp) -> true;
        String trimmedInput = caseInsensitive ?
                input.trim().toLowerCase(Locale.ROOT) : input.trim();
        return inputFitsView.test(trimmedInput);
    }

    /**
     * Добавляет в конец предиката требование НЕ учитывать регистр символов.
     * По умолчанию, все предикаты не учитывают регистр строк.
     * Например, предикату new InputPredicate().caseInsensitive().has("при").and().has("вет")
     * удовлетворяют как строка "привет", так и строка "ПРИВЕТ".
     * @return Обновленный предикат.
     */
    public InputPredicate caseInsensitive() {
        return new InputPredicate(predicateComposition, substringComposition,
                isNegated, true, inputFitsView);
    }

    /**
     * Добавляет в конец предиката требование учитывать регистр символов.
     * Например, предикату new InputPredicate().has("при").and().has("вет")
     * удовлетворяют как строка "привет", так и строка "ПРИВЕТ".
     * Предикату new InputPredicate().caseSensitive().has("при").and().has("вет")
     * удовлетворяет только строка "привет" и не удовлетворяет строка "ПРИВЕТ".
     * @return Обновленный предикат.
     */
    public InputPredicate caseSensitive() {
        return new InputPredicate(predicateComposition, substringComposition,
                isNegated, false, inputFitsView);
    }

    /**
     * Добавляет в конец предиката оператор "OR".
     * @return Обновленный предикат.
     */
    public InputPredicate or() {
        predicateComposition = PredicateComposition.Or;
        return compositePredicateWith();
    }

    /**
     * Добавляет в конец предиката оператор "AND".
     * @return Обновленный предикат.
     */
    public InputPredicate and() {
        predicateComposition = PredicateComposition.And;
        return compositePredicateWith();
    }

    /**
     * Добавляет в конец предиката требование, что он строка должна
     * содержать в себе указанную подстроку.
     * @return Обновленный предикат.
     */
    public InputPredicate has(String substring) {
        return any(substring);
    }

    /**
     * Добавляет в конец предиката требование, что строка должна
     * содержать в себе любую из указанных подстрок.
     * @return Обновленный предикат.
     */
    public InputPredicate any(String... substrings) {
        substringComposition = SubstringComposition.Any;
        return compositePredicateWith(substrings);
    }

    /**
     * Добавляет в конец предиката требование, что строка должна
     * содержать в себе каждую из указанных подстрок.
     * @return Обновленный предикат.
     */
    public InputPredicate all(String... substrings) {
        if (substrings == null || substrings.length <= 0)
            throw new IllegalArgumentException("The substrings to test cannot be null or empty.");
        substringComposition = SubstringComposition.All;
        return compositePredicateWith(substrings);
    }

    /**
     * Добавляет в конец предиката оператор "NOT".
     * @return Обновленный предикат.
     */
    public InputPredicate not() {
        isNegated = !isNegated;
        return compositePredicateWith();
    }

    /**
     * Конструирует и возвращает новый предикат на основе этого, добавив ему
     * в конце требование, что строка должна содержать любую из указанных подстрок,
     * если в предикате были затребованы любые, либо каждую - если были затребованы
     * каждые (это требование зависит от параметра SubstringComposition).
     * Оператор "NOT" на конце предиката также учитывается: если он присутствует,
     * требование в предикат добавляется с отрицанием, иначе - без отрицания.
     * @param substrings подстроки, которые предикат требует, чтобы они находились
     *                   в строке (любая или каждая из них).
     * @return Обновленный предикат.
     */
    private InputPredicate compositePredicateWith(String... substrings) {
        if (predicateComposition == PredicateComposition.None)
            throw new IllegalArgumentException("Predicate composition (`or`/`and`) type is not specified.");
        if (substringComposition == SubstringComposition.None)
            throw new IllegalArgumentException("Substring composition (`any`/`all`) type is not specified.");
        if (inputFitsView == null)
            inputFitsView = getEmptyPredicate();
        if (substrings == null || substrings.length <= 0)
            return this;
        String[] newSubstrings = caseInsensitive ?
                Arrays.stream(substrings).map(s -> s.toLowerCase(Locale.ROOT)).toArray(String[]::new) : substrings;
        var containsSubstrings = getContainsSubstringsPredicate(newSubstrings);
        inputFitsView = getCombinedPredicate(containsSubstrings);
        return new InputPredicate(predicateComposition,
                SubstringComposition.Any, isNegated, caseInsensitive, inputFitsView);
    }

    /**
     * Возвращает пустой предикат от строки, т.е. не содержащий в себе каких-либо
     * требований к передаваемой строке. Этот метод, по сути, является конструктором
     * нового требования к строке.
     * @return Пустой предикат от строки String.
     */
    private Predicate<String> getEmptyPredicate() {
        if (inputFitsView != null)
            return (input) -> false;
        if (predicateComposition == PredicateComposition.Or)
            return (input) -> false;
        else if (predicateComposition == PredicateComposition.And)
            return (input) -> true;
        else
            throw new IllegalArgumentException("Invalid predicate composition type.");
    }

    /**
     * Создает и возвращает требование (являющееся предикатом от строки),
     * что строка должна содержать любую из указанных подстрок,
     * если в предикате были затребованы любые, либо каждую - если были затребованы
     * каждые (это требование зависит от параметра SubstringComposition).
     * @param substrings подстроки, которые предикат требует, чтобы они находились
     *                   в строке (любая или каждая из них).
     * @return Новый предикат от строки String. Если массив указанных подстрок
     * пуст (т.е. никакие подстроки не затребованы) возвращает пустой предикат.
     */
    private Predicate<String> getContainsSubstringsPredicate(String... substrings) {
        if (substrings == null || substrings.length <= 0)
            return getEmptyPredicate();
        if (substringComposition == SubstringComposition.Any)
            return (input) -> inputContainsAny(input, substrings);
        else if (substringComposition == SubstringComposition.All)
            return  (input) -> inputContainsAll(input, substrings);
        else
            throw new IllegalArgumentException("Invalid substring composition type.");
    }

    /**
     * Объединяет указанное требование (являющееся предикатом от строки)
     * с требованиями этого предиката и возвращает объединенный предикат.
     * Объединение происходит по последнему оператору предиката: "OR"/"AND".
     * Например, если на конце этого предиката находится оператор "OR",
     * то объединенный предикат будет выглядеть так:
     * { предыдущее требование 1, 2, ..., n } OR { указанное требование }
     * @param other требование к строке, которое будет присоединено ко
     *              всем предыдущим требованиям предиката.
     * @return Возвращает объединенный предикат от строки String, состоящий
     * из предыдущих требований этого предиката и указанного требования.
     */
    private Predicate<String> getCombinedPredicate(Predicate<String> other) {
        var otherPredicate = isNegated ? other.negate() : other;
        if (predicateComposition == PredicateComposition.Or)
            return inputFitsView.or(otherPredicate);
        else if (predicateComposition == PredicateComposition.And)
            return inputFitsView.and(otherPredicate);
        else
            throw new IllegalArgumentException("Invalid predicate composition type.");
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