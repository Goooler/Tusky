package com.keylesspalace.tusky

import android.text.Spannable
import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import com.keylesspalace.tusky.util.highlightSpans
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Enclosed::class)
class SpanUtilsTest {
    @Test
    fun matchesMixedSpans() {
        val input = "one #one two: @two three : https://thr.ee/meh?foo=bar&wat=@at#hmm four #four five @five ろく#six"
        val inputSpannable = FakeSpannable(input)
        highlightSpans(inputSpannable, 0xffffff)
        val spans = inputSpannable.spans
        assertThat(spans.size).isEqualTo(6)
    }

    @Test
    fun doesntMergeAdjacentURLs() {
        val firstURL = "http://first.thing"
        val secondURL = "https://second.thing"
        val inputSpannable = FakeSpannable("$firstURL $secondURL")
        highlightSpans(inputSpannable, 0xffffff)
        val spans = inputSpannable.spans
        assertThat(spans.size).isEqualTo(2)
        assertThat(firstURL.length).isEqualTo(spans[0].end - spans[0].start)
        assertThat(secondURL.length).isEqualTo(spans[1].end - spans[1].start)
    }

    @RunWith(Parameterized::class)
    class MatchingTests(private val thingToHighlight: String) {
        companion object {
            @Parameterized.Parameters(name = "{0}")
            @JvmStatic
            fun data(): Iterable<Any> {
                return listOf(
                    "@mention",
                    "#tag",
                    "#tåg",
                    "https://thr.ee/meh?foo=bar&wat=@at#hmm",
                    "http://thr.ee/meh?foo=bar&wat=@at#hmm"
                )
            }
        }

        @Test
        fun matchesSpanAtStart() {
            val inputSpannable = FakeSpannable(thingToHighlight)
            highlightSpans(inputSpannable, 0xffffff)
            val spans = inputSpannable.spans
            assertThat(spans.size).isEqualTo(1)
            assertThat(thingToHighlight.length).isEqualTo(spans[0].end - spans[0].start)
        }

        @Test
        fun matchesSpanNotAtStart() {
            val inputSpannable = FakeSpannable(" $thingToHighlight")
            highlightSpans(inputSpannable, 0xffffff)
            val spans = inputSpannable.spans
            assertThat(spans.size).isEqualTo(1)
            assertThat(thingToHighlight.length).isEqualTo(spans[0].end - spans[0].start)
        }

        @Test
        fun doesNotMatchSpanEmbeddedInText() {
            val inputSpannable = FakeSpannable("aa${thingToHighlight}aa")
            highlightSpans(inputSpannable, 0xffffff)
            val spans = inputSpannable.spans
            assertThat(spans).isEmpty()
        }

        @Test
        fun doesNotMatchSpanEmbeddedInAnotherSpan() {
            val inputSpannable = FakeSpannable("@aa${thingToHighlight}aa")
            highlightSpans(inputSpannable, 0xffffff)
            val spans = inputSpannable.spans
            assertThat(spans.size).isEqualTo(1)
        }

        @Test
        fun spansDoNotOverlap() {
            val begin = "@begin"
            val end = "#end"
            val inputSpannable = FakeSpannable("$begin $thingToHighlight $end")
            highlightSpans(inputSpannable, 0xffffff)
            val spans = inputSpannable.spans
            assertThat(spans.size).isEqualTo(3)

            val middleSpan = spans.single { span -> span.start > 0 && span.end < inputSpannable.lastIndex }
            assertThat(begin.length + 1).isEqualTo(middleSpan.start)
            assertThat(inputSpannable.length - end.length - 1).isEqualTo(middleSpan.end)
        }
    }

    @RunWith(Parameterized::class)
    class HighlightingTestsForTag(
        private val text: String,
        private val expectedStartIndex: Int,
        private val expectedEndIndex: Int
    ) {
        companion object {
            @Parameterized.Parameters(name = "{0}")
            @JvmStatic
            fun data(): Iterable<Any> {
                return listOf(
                    arrayOf("#test", 0, 5),
                    arrayOf(" #AfterSpace", 1, 12),
                    arrayOf("#BeforeSpace ", 0, 12),
                    arrayOf("@#after_at", 1, 10),
                    arrayOf("あいうえお#after_hiragana", 5, 20),
                    arrayOf("##DoubleHash", 1, 12),
                    arrayOf("###TripleHash", 2, 13)
                )
            }
        }

        @Test
        fun matchExpectations() {
            val inputSpannable = FakeSpannable(text)
            highlightSpans(inputSpannable, 0xffffff)
            val spans = inputSpannable.spans
            assertThat(spans.size).isEqualTo(1)
            val span = spans.first()
            assertThat(span.span).isEqualTo(expectedStartIndex)
            assertThat(span.end).isEqualTo(expectedEndIndex)
        }
    }

    class FakeSpannable(private val text: String) : Spannable {
        val spans = mutableListOf<BoundedSpan>()

        override fun setSpan(what: Any?, start: Int, end: Int, flags: Int) {
            spans.add(BoundedSpan(what, start, end))
        }

        override fun <T : Any> getSpans(start: Int, end: Int, type: Class<T>): Array<T> {
            return spans.filter { it.start >= start && it.end <= end && type.isInstance(it.span) }
                .map { it.span }
                .toTypedArray() as Array<T>
        }

        override fun removeSpan(what: Any?) {
            spans.removeIf { span -> span.span == what }
        }

        override fun toString(): String {
            return text
        }

        override val length: Int
            get() = text.length

        class BoundedSpan(val span: Any?, val start: Int, val end: Int)

        override fun nextSpanTransition(start: Int, limit: Int, type: Class<*>?): Int {
            throw NotImplementedError()
        }

        override fun getSpanEnd(tag: Any?): Int {
            throw NotImplementedError()
        }

        override fun getSpanFlags(tag: Any?): Int {
            throw NotImplementedError()
        }

        override fun get(index: Int): Char {
            throw NotImplementedError()
        }

        override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
            throw NotImplementedError()
        }

        override fun getSpanStart(tag: Any?): Int {
            throw NotImplementedError()
        }
    }
}
