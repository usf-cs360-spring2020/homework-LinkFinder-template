package edu.usfca.cs272;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.ClassOrderer.ClassName;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * Tests the {@link LinkFinder} class.
 *
 * @see LinkFinder
 *
 * @author CS 272 Software Development (University of San Francisco)
 * @version Spring 2023
 */
@TestClassOrder(ClassName.class)
public class LinkFinderTest {
	// ███████╗████████╗ ██████╗ ██████╗
	// ██╔════╝╚══██╔══╝██╔═══██╗██╔══██╗
	// ███████╗   ██║   ██║   ██║██████╔╝
	// ╚════██║   ██║   ██║   ██║██╔═══╝
	// ███████║   ██║   ╚██████╔╝██║
	// ╚══════╝   ╚═╝    ╚═════╝ ╚═╝
	/*
	 * ...and read this. The remote tests will NOT run unless you are passing the
	 * local tests first. You should not try to run the remote tests until the local
	 * tests are passing, and should avoid rapidly re-running the remote tests over
	 * and over again. You risk being blocked by our web server for making too many
	 * requests!
	 */

	/**
	 * Group of local tests.
	 */
	@Nested
	@TestMethodOrder(MethodName.class)
	public class LocalTests {
		/**
		 * Tests links on locally-created HTML text (not actual webpages).
		 */
		@Nested
		@TestMethodOrder(OrderAnnotation.class)
		public class A_LocalValidTests {
			/**
			 * Tests a single link.
			 *
			 * @see LinkFinder#listUrls(URL, String)
			 * @throws MalformedURLException if unable to create URLs
			 */
			@Test
			@Order(1)
			public void testSimple() throws MalformedURLException {
				String link = "http://www.usfca.edu/";
				String html = """
						<a href="http://www.usfca.edu/">
						""";

				testValid(link, html);
			}

			/**
			 * Tests a single link.
			 *
			 * @see LinkFinder#listUrls(URL, String)
			 * @throws MalformedURLException if unable to create URLs
			 */
			@Test
			@Order(2)
			public void testFragment() throws MalformedURLException {
				String link = "http://docs.python.org/library/string.html?highlight=string";
				String html = """
						<a href="http://docs.python.org/library/string.html?highlight=string#module-string">
						""";

				testValid(link, html);
			}

			/**
			 * Tests a single link.
			 *
			 * @see LinkFinder#listUrls(URL, String)
			 * @throws MalformedURLException if unable to create URLs
			 */
			@Test
			@Order(3)
			public void testUppercase() throws MalformedURLException {
				String link = "HTTP://WWW.USFCA.EDU";
				String html = """
						<A HREF="HTTP://WWW.USFCA.EDU">
						""";

				testValid(link, html);
			}

			/**
			 * Tests a single link.
			 *
			 * @see LinkFinder#listUrls(URL, String)
			 * @throws MalformedURLException if unable to create URLs
			 */
			@Test
			@Order(4)
			public void testMixedCase() throws MalformedURLException {
				String link = "http://www.usfca.edu";
				String html = """
						<A hREf="http://www.usfca.edu">
						""";

				testValid(link, html);
			}

			/**
			 * Tests a single link.
			 *
			 * @see LinkFinder#listUrls(URL, String)
			 * @throws MalformedURLException if unable to create URLs
			 */
			@Test
			@Order(5)
			public void testSpaces() throws MalformedURLException {
				String link = "http://www.usfca.edu";
				String html = """
						<a href = "http://www.usfca.edu" >
						""";

				testValid(link, html);
			}

			/**
			 * Tests a single link.
			 *
			 * @see LinkFinder#listUrls(URL, String)
			 * @throws MalformedURLException if unable to create URLs
			 */
			@Test
			@Order(6)
			public void testOneNewline() throws MalformedURLException {
				String link = "http://www.usfca.edu";
				String html = """
						<a href =
							"http://www.usfca.edu">
						""";

				testValid(link, html);
			}

			/**
			 * Tests a single link.
			 *
			 * @see LinkFinder#listUrls(URL, String)
			 * @throws MalformedURLException if unable to create URLs
			 */
			@Test
			@Order(7)
			public void testManyNewlines() throws MalformedURLException {
				String link = "http://www.usfca.edu";
				String html = """
						<a

						href
						=
						"http://www.usfca.edu"
						>
						""";

				testValid(link, html);
			}

			/**
			 * Tests a single link.
			 *
			 * @see LinkFinder#listUrls(URL, String)
			 * @throws MalformedURLException if unable to create URLs
			 */
			@Test
			@Order(8)
			public void testSnippet() throws MalformedURLException {
				String link = "http://www.usfca.edu";
				String html = """
						<p>
							<a href="http://www.usfca.edu">USFCA</a>
							is in San Francisco.
						</p>
						""";

				testValid(link, html);
			}

			/**
			 * Tests a single link.
			 *
			 * @see LinkFinder#listUrls(URL, String)
			 * @throws MalformedURLException if unable to create URLs
			 */
			@Test
			@Order(9)
			public void testRelative() throws MalformedURLException {
				String link = "http://www.example.com/index.html";
				String html = """
						<a href="index.html">
						""";

				testValid(link, html);
			}

			/**
			 * Tests a single link.
			 *
			 * @see LinkFinder#listUrls(URL, String)
			 * @throws MalformedURLException if unable to create URLs
			 */
			@Test
			@Order(10)
			public void testHREFLast() throws MalformedURLException {
				String link = "http://www.example.com/index.html";
				String html = """
						<a name="home" href="index.html">
						""";

				testValid(link, html);
			}

			/**
			 * Tests a single link.
			 *
			 * @see LinkFinder#listUrls(URL, String)
			 * @throws MalformedURLException if unable to create URLs
			 */
			@Test
			@Order(11)
			public void testHREFFirst() throws MalformedURLException {
				String link = "http://www.example.com/index.html";
				String html = """
						<a href="index.html" class="primary">
						""";

				testValid(link, html);
			}

			/**
			 * Tests a single link.
			 *
			 * @see LinkFinder#listUrls(URL, String)
			 * @throws MalformedURLException if unable to create URLs
			 */
			@Test
			@Order(12)
			public void testMultipleAttributes() throws MalformedURLException {
				String link = "http://www.example.com/index.html";
				String html = """
						<a name="home" target="_top" href="index.html" id="home" accesskey="A">
						""";
				testValid(link, html);
			}
		}

		/**
		 * Tests links on locally-created HTML text (not actual webpages).
		 */
		@Nested
		@TestMethodOrder(OrderAnnotation.class)
		public class B_LocalInvalidTests {
			/**
			 * Tests a single link.
			 *
			 * @see LinkFinder#listUrls(URL, String)
			 * @throws MalformedURLException if unable to create URLs
			 */
			@Test
			@Order(1)
			public void testNoHREF() throws MalformedURLException {
				String html = """
						<a name = "home">
						""";

				testInvalid(html);
			}

			/**
			 * Tests a single link.
			 *
			 * @see LinkFinder#listUrls(URL, String)
			 * @throws MalformedURLException if unable to create URLs
			 */
			@Test
			@Order(2)
			public void testNoAnchor() throws MalformedURLException {
				String html = """
						<h1>Home</h1>
						""";

				testInvalid(html);
			}

			/**
			 * Tests a single link.
			 *
			 * @see LinkFinder#listUrls(URL, String)
			 * @throws MalformedURLException if unable to create URLs
			 */
			@Test
			@Order(3)
			public void testMixedNoHREF() throws MalformedURLException {
				String html = """
						<a name=href>The href = "link" attribute is useful.</a>
						""";

				testInvalid(html);
			}

			/**
			 * Tests a single link.
			 *
			 * @see LinkFinder#listUrls(URL, String)
			 * @throws MalformedURLException if unable to create URLs
			 */
			@Test
			@Order(4)
			public void testLinkTag() throws MalformedURLException {
				String html = """
						<link rel="stylesheet" type="text/css" href="style.css">
						""";

				testInvalid(html);
			}

			/**
			 * Tests a single link.
			 *
			 * @see LinkFinder#listUrls(URL, String)
			 * @throws MalformedURLException if unable to create URLs
			 */
			@Test
			@Order(5)
			public void testNoTag() throws MalformedURLException {
				String html = """
						<p>The a href="http://www.google.com" attribute is often used in HTML.</p>"
						""";

				testInvalid(html);
			}

			/**
			 * Tests a single link.
			 *
			 * @see LinkFinder#listUrls(URL, String)
			 * @throws MalformedURLException if unable to create URLs
			 */
			@Test
			@Order(6)
			public void testJavascript() throws MalformedURLException {
				String html = """
						<a href="javascript:alert('Hello!');">Say hello!</a>
						""";

				testInvalid(html);
			}

			/**
			 * Tests a single link.
			 *
			 * @see LinkFinder#listUrls(URL, String)
			 * @throws MalformedURLException if unable to create URLs
			 */
			@Test
			@Order(7)
			public void testEmail() throws MalformedURLException {
				String html = """
						<a href="mailto:sjengle@cs.usfca.edu">sjengle@cs.usfca.edu</a>
						""";

				testInvalid(html);
			}
		}

		/**
		 * Tests links on locally-created HTML text (not actual webpages).
		 */
		@Nested
		@TestMethodOrder(OrderAnnotation.class)
		public class C_LocalMultipleTests {
			/**
			 * Tests multiple links within the HTML text.
			 *
			 * @see LinkFinder#listUrls(URL, String)
			 * @throws MalformedURLException if unable to create URLs
			 */
			@Test
			@Order(1)
			public void testMultiple() throws MalformedURLException {
				String html = """
						<h1><a name="about">About</a></h1>

						<p>The <a class="primary" href="index.html">Department of
						Computer Science</a> offers an undergraduate and graduate degree at
						<a href="http://www.usfca.edu">University of San Francisco</a>.</p>

						<p>Interested? To find out more about those degrees, visit the URL
						<a href="https://www.usfca.edu/catalog/undergraduate/arts-sciences/computer-science">
						https://www.usfca.edu/catalog/undergraduate/arts-sciences/computer-science</a>.</p>
						""";

				List<URL> expected = List.of(
						new URL("https://www.cs.usfca.edu/index.html"),
						new URL("http://www.usfca.edu"),
						new URL("https://www.usfca.edu/catalog/undergraduate/arts-sciences/computer-science"));

				URL base = new URL("https://www.cs.usfca.edu/");
				ArrayList<URL> actual = LinkFinder.listUrls(base, html);

				Supplier<String> debug = () -> "%nHTML:%n%s%n%n".formatted(html);
				Assertions.assertEquals(expected, actual, debug);
			}
		}
	}

	/**
	 * Group of remote tests.
	 */
	@Nested
	@TestMethodOrder(MethodName.class)
	@TestInstance(Lifecycle.PER_CLASS)
	public class RemoteTests {
		/**
		 * Tests links on actual webpages.
		 */
		@Nested
		@TestMethodOrder(OrderAnnotation.class)
		public class D_RemoteSimpleTests {
			/**
			 * Tests listing links for a remote URL.
			 *
			 * @throws MalformedURLException if unable to create URLs
			 */
			@Test
			@Order(1)
			public void testHello() throws MalformedURLException {
				ArrayList<URL> expected = new ArrayList<>();
				testRemote("input/simple/hello.html", expected);
			}

			/**
			 * Tests listing links for a remote URL.
			 *
			 * @throws MalformedURLException if unable to create URLs
			 */
			@Test
			@Order(2)
			public void testSimple() throws MalformedURLException {
				URL base = new URL(GITHUB);

				List<URL> expected = List.of(
						new URL(base, "input/simple/a/b/c/subdir.html"),
						new URL(base, "input/simple/capital_extension.HTML"),
						new URL(base, "input/simple/double_extension.html.txt"),
						new URL(base, "input/simple/empty.html"),
						new URL(base, "input/simple/hello.html"),
						new URL(base, "input/simple/mixed_case.htm"),
						new URL(base, "input/simple/no_extension"),
						new URL(base, "input/simple/no_extension"),
						new URL(base, "input/simple/position.html"),
						new URL(base, "input/simple/stems.html"),
						new URL(base, "input/simple/symbols.html"),
						new URL(base, "input/simple/dir.txt"),
						new URL(base, "input/simple/wrong_extension.html"));

				testRemote("input/simple/index.html", expected);
			}

			/**
			 * Tests listing links for a remote URL.
			 *
			 * @throws MalformedURLException if unable to create URLs
			 */
			@Test
			@Order(3)
			public void testBirds() throws MalformedURLException {
				List<URL> expected = getBirdURLs();
				testRemote("input/birds/index.html", expected);
			}
		}

		/**
		 * Tests links on actual webpages.
		 */
		@Nested
		@TestMethodOrder(OrderAnnotation.class)
		public class E_RemoteGutenTests {
			/**
			 * Tests listing links for a remote URL.
			 *
			 * @throws MalformedURLException if unable to create URLs
			 */
			@Test
			@Order(1)
			public void testGuten1400() throws MalformedURLException {
				URL base = new URL(GITHUB);
				URL guten = new URL(base, "input/guten/1400-h/");
				List<URL> copies = Collections.nCopies(59, new URL(guten, "1400-h.htm"));
				List<URL> images = List.of(
						new URL(guten, "images/0012.jpg"), new URL(guten, "images/0037.jpg"),
						new URL(guten, "images/0072.jpg"), new URL(guten, "images/0082.jpg"),
						new URL(guten, "images/0132.jpg"), new URL(guten, "images/0189.jpg"),
						new URL(guten, "images/0223.jpg"), new URL(guten, "images/0242.jpg"),
						new URL(guten, "images/0245.jpg"), new URL(guten, "images/0279.jpg"),
						new URL(guten, "images/0295.jpg"), new URL(guten, "images/0335.jpg"),
						new URL(guten, "images/0348.jpg"), new URL(guten, "images/0393.jpg"),
						new URL(guten, "images/0399.jpg"));

				List<URL> expected = new ArrayList<>();
				expected.addAll(copies); // appears 59 times in table of contents
				expected.addAll(images); // followed by many images

				testRemote("input/guten/1400-h/1400-h.htm", expected);
			}

			/**
			 * Tests listing links for a remote URL.
			 *
			 * @throws MalformedURLException if unable to create URLs
			 */
			@Test
			@Order(2)
			public void testGutenberg() throws MalformedURLException {
				List<URL> expected = getGutenURLs();
				testRemote("input/guten/index.html", expected);
			}
		}

		/**
		 * Tests links on actual webpages.
		 */
		@Nested
		@TestMethodOrder(OrderAnnotation.class)
		public class F_RemoteRecurseTests {
			/**
			 * Tests listing links for a remote URL.
			 *
			 * @throws MalformedURLException if unable to create URLs
			 */
			@Test
			@Order(1)
			public void testRecurse() throws MalformedURLException {
				URL link02 = new URL("https://www.cs.usfca.edu/~cs272/recurse/link02.html");
				List<URL> expected = List.of(link02);

				testRemote("https://www.cs.usfca.edu/~cs272/recurse/link01.html", expected);
			}
		}

		/**
		 * Cancels running these tests until the local ones fully pass.
		 */
		@BeforeAll
		public void setup() {
			Class<LocalTests> test = LocalTests.class;
			int tests = 0;

			// Code to programmatically call all of the local tests
			// Includes tests in the nested classes
			for (var nested : test.getClasses()) {
				for (var method : nested.getMethods()) {
					if (method.getAnnotation(Test.class) != null) {
						try {
							var outer = new LocalTests();
							var inner = nested.getDeclaredConstructor(test).newInstance(outer);
							method.invoke(inner);
							tests++;
						}
						catch (Exception e) {
							String debug = nested.getSimpleName() + "." + method.getName() + "()";
							Assertions.fail("Cannot verify local tests pass. See: " + debug, e);
						}
					}
				}
			}

			System.out.println("All " + tests + " local tests pass.");
		}
	}

	/**
	 * Tests a valid link.
	 *
	 * @param link the link that should be found
	 * @param html the html to parse for that link
	 * @throws MalformedURLException if unable to create URLs
	 */
	public static void testValid(String link, String html) throws MalformedURLException {
		URL base = new URL("http://www.example.com");
		URL expected = new URL(link);
		URL actual = LinkFinder.listUrls(base, html).get(0);

		String format = "%nHTML:%n%s%nExpected:%n%s%n%nActual:%n%s%n%n";
		Supplier<String> debug = () -> format.formatted(html, expected, actual);
		Assertions.assertEquals(expected, actual, debug);
	}

	/**
	 * Tests an invalid link.
	 *
	 * @param html the html to parse without a valid link
	 * @throws MalformedURLException if unable to create URLs
	 */
	public static void testInvalid(String html) throws MalformedURLException {
		URL base = new URL("http://www.example.com");
		ArrayList<URL> actual = LinkFinder.listUrls(base, html);

		String format = "%nHTML:%n%s%nExpected:%n[]%n%nActual:%n%s%n%n";
		Supplier<String> debug = () -> format.formatted(html, actual);
		Assertions.assertEquals(0, actual.size(), debug);
	}

	/**
	 * Tests link parsing from a remote URL.
	 *
	 * @param url the url to fetch
	 * @param expected the expected links
	 * @throws MalformedURLException if unable to create URLs
	 */
	public static void testRemote(String url, List<URL> expected) throws MalformedURLException {
		URL base = new URL(GITHUB);
		URL link = new URL(base, url);

		Assertions.assertTimeoutPreemptively(Duration.ofSeconds(30), () -> {
			String html = getHTML(link);
			List<URL> actual = LinkFinder.listUrls(link, html);

			// compare strings for easier side-by-side debugging
			var joiner = Collectors.joining("\n");
			String expectedText = expected.stream().map(URL::toString).collect(joiner);
			String actualText = actual.stream().map(URL::toString).collect(joiner);

			String debug = "Use the \"Result Comparison\" feature in Eclipse to compare results side-by-side.\n";
			Assertions.assertEquals(expectedText, actualText, debug);
		});
	}

	/**
	 * Helper method to get the expected URLs from a remote webpage.
	 *
	 * @return list of expected URLs
	 * @throws MalformedURLException if unable to create URLs
	 */
	public static final List<URL> getBirdURLs() throws MalformedURLException {
		URL base = new URL(GITHUB);

		return List.of(
				new URL(base, "input/birds/albatross.html"),
				new URL(base, "input/birds/birds.html"),
				new URL(base, "input/birds/blackbird.html"),
				new URL(base, "input/birds/bluebird.html"),
				new URL(base, "input/birds/cardinal.html"),
				new URL(base, "input/birds/chickadee.html"),
				new URL(base, "input/birds/crane.html"),
				new URL(base, "input/birds/crow.html"),
				new URL(base, "input/birds/cuckoo.html"),
				new URL(base, "input/birds/dove.html"),
				new URL(base, "input/birds/duck.html"),
				new URL(base, "input/birds/eagle.html"),
				new URL(base, "input/birds/egret.html"),
				new URL(base, "input/birds/falcon.html"),
				new URL(base, "input/birds/finch.html"),
				new URL(base, "input/birds/goose.html"),
				new URL(base, "input/birds/gull.html"),
				new URL(base, "input/birds/hawk.html"),
				new URL(base, "input/birds/heron.html"),
				new URL(base, "input/birds/hummingbird.html"),
				new URL(base, "input/birds/ibis.html"),
				new URL(base, "input/birds/kingfisher.html"),
				new URL(base, "input/birds/loon.html"),
				new URL(base, "input/birds/magpie.html"),
				new URL(base, "input/birds/mallard.html"),
				new URL(base, "input/birds/meadowlark.html"),
				new URL(base, "input/birds/mockingbird.html"),
				new URL(base, "input/birds/nighthawk.html"),
				new URL(base, "input/birds/osprey.html"),
				new URL(base, "input/birds/owl.html"),
				new URL(base, "input/birds/pelican.html"),
				new URL(base, "input/birds/pheasant.html"),
				new URL(base, "input/birds/pigeon.html"),
				new URL(base, "input/birds/puffin.html"),
				new URL(base, "input/birds/quail.html"),
				new URL(base, "input/birds/raven.html"),
				new URL(base, "input/birds/roadrunner.html"),
				new URL(base, "input/birds/robin.html"),
				new URL(base, "input/birds/sandpiper.html"),
				new URL(base, "input/birds/sparrow.html"),
				new URL(base, "input/birds/starling.html"),
				new URL(base, "input/birds/stork.html"),
				new URL(base, "input/birds/swallow.html"),
				new URL(base, "input/birds/swan.html"),
				new URL(base, "input/birds/tern.html"),
				new URL(base, "input/birds/turkey.html"),
				new URL(base, "input/birds/vulture.html"),
				new URL(base, "input/birds/warbler.html"),
				new URL(base, "input/birds/woodpecker.html"),
				new URL(base, "input/birds/wren.html"),
				new URL(base, "input/birds/yellowthroat.html"),
				new URL(base, "input/birds/nowhere.html"),
				new URL(base, "input/birds/"));
	}

	/**
	 * Helper method to get the expected URLs from a remote webpage.
	 *
	 * @return list of expected URLs
	 * @throws MalformedURLException if unable to create URLs
	 */
	public static List<URL> getGutenURLs() throws MalformedURLException {
		URL base = new URL(GITHUB);
		return List.of(
				new URL(base, "input/guten/1400-h/1400-h.htm"),
				new URL(base, "input/guten/2701-h/2701-h.htm"),
				new URL(base, "input/guten/50468-h/50468-h.htm"),
				new URL(base, "input/guten/1322-h/1322-h.htm"),
				new URL(base, "input/guten/1228-h/1228-h.htm"), // present unless removing comments
				new URL(base, "input/guten/1661-h/1661-h.htm"),
				new URL(base, "input/guten/22577-h/22577-h.htm"),
				new URL(base, "input/guten/37134-h/37134-h.htm"));
	}

	/**
	 * Helper method to get the HTML from a URL. (Cannot be used for projects.)
	 *
	 * @param url the url to fetch
	 * @return the HTML as a String object
	 * @throws IOException if unable to get HTML
	 */
	public static String getHTML(URL url) throws IOException {
		try (InputStream input = url.openStream()) {
			return new String(input.readAllBytes(), StandardCharsets.UTF_8);
		}
	}

	/** Base URL for the GitHub test website. */
	public static final String GITHUB = "https://usf-cs272-spring2023.github.io/project-web/";
}
