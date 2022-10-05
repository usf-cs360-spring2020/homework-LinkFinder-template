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
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import org.junit.jupiter.api.TestMethodOrder;

/**
 * Tests the {@link LinkFinder} class.
 *
 * @see LinkFinder
 *
 * @author CS 272 Software Development (University of San Francisco)
 * @version Fall 2022
 */
@TestMethodOrder(MethodName.class)
public class LinkFinderTest {
	/*
	 * ███████╗████████╗ ██████╗ ██████╗
	 * ██╔════╝╚══██╔══╝██╔═══██╗██╔══██╗
	 * ███████╗   ██║   ██║   ██║██████╔╝
	 * ╚════██║   ██║   ██║   ██║██╔═══╝
	 * ███████║   ██║   ╚██████╔╝██║
	 * ╚══════╝   ╚═╝    ╚═════╝ ╚═╝
	 *
	 * ...and read this. The remote tests will NOT run unless you are
	 * passing the local tests first. You should not try to run the
	 * remote tests until the local tests are passing, and should avoid
	 * rapidly re-running the remote tests over and over again. You risk
	 * being blocked by our web server for making too many requests!
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
				testRemote("https://www.cs.usfca.edu/~cs272/simple/hello.html", expected);
			}

			/**
			 * Tests listing links for a remote URL.
			 *
			 * @throws MalformedURLException if unable to create URLs
			 */
			@Test
			@Order(2)
			public void testSimple() throws MalformedURLException {
				List<URL> expected = List.of(
						new URL("https://www.cs.usfca.edu/~cs272/simple/a/b/c/subdir.html"),
						new URL("https://www.cs.usfca.edu/~cs272/simple/capital_extension.HTML"),
						new URL("https://www.cs.usfca.edu/~cs272/simple/double_extension.html.txt"),
						new URL("https://www.cs.usfca.edu/~cs272/simple/empty.html"),
						new URL("https://www.cs.usfca.edu/~cs272/simple/hello.html"),
						new URL("https://www.cs.usfca.edu/~cs272/simple/mixed_case.htm"),
						new URL("https://www.cs.usfca.edu/~cs272/simple/no_extension"),
						new URL("https://www.cs.usfca.edu/~cs272/simple/position.html"),
						new URL("https://www.cs.usfca.edu/~cs272/simple/symbols.html"));

				testRemote("https://www.cs.usfca.edu/~cs272/simple/index.html", expected);
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
				testRemote("https://www.cs.usfca.edu/~cs272/birds/birds.html", expected);
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
				URL base = new URL("https://www.cs.usfca.edu/~cs272/guten/1400-h/");
				List<URL> copies = Collections.nCopies(59, new URL(base, "1400-h.htm"));
				List<URL> images = List.of(
						new URL(base, "images/0012.jpg"), new URL(base, "images/0037.jpg"),
						new URL(base, "images/0072.jpg"), new URL(base, "images/0082.jpg"),
						new URL(base, "images/0132.jpg"), new URL(base, "images/0189.jpg"),
						new URL(base, "images/0223.jpg"), new URL(base, "images/0242.jpg"),
						new URL(base, "images/0245.jpg"), new URL(base, "images/0279.jpg"),
						new URL(base, "images/0295.jpg"), new URL(base, "images/0335.jpg"),
						new URL(base, "images/0348.jpg"), new URL(base, "images/0393.jpg"),
						new URL(base, "images/0399.jpg"));

				List<URL> expected = new ArrayList<>();
				expected.addAll(copies); // appears 59 times in table of contents
				expected.addAll(images); // followed by many images

				testRemote("https://www.cs.usfca.edu/~cs272/guten/1400-h/1400-h.htm", expected);
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
				testRemote("https://www.cs.usfca.edu/~cs272/guten/index.html", expected);
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
		URL base = new URL(url);

		Assertions.assertTimeout(Duration.ofSeconds(30), () -> {
			String html = getHTML(base);
			List<URL> actual = LinkFinder.listUrls(base, html);

			// compare strings for easier side-by-side debugging
			String expectedText = expected.stream().map(u -> u.toString()).collect(Collectors.joining("\n"));
			String actualText = actual.stream().map(u -> u.toString()).collect(Collectors.joining("\n"));

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
		return List.of(
				new URL("https://www.cs.usfca.edu/~cs272/birds/albatross.html"),
				new URL("https://www.cs.usfca.edu/~cs272/birds/blackbird.html"),
				new URL("https://www.cs.usfca.edu/~cs272/birds/bluebird.html"),
				new URL("https://www.cs.usfca.edu/~cs272/birds/cardinal.html"),
				new URL("https://www.cs.usfca.edu/~cs272/birds/chickadee.html"),
				new URL("https://www.cs.usfca.edu/~cs272/birds/crane.html"),
				new URL("https://www.cs.usfca.edu/~cs272/birds/crow.html"),
				new URL("https://www.cs.usfca.edu/~cs272/birds/cuckoo.html"),
				new URL("https://www.cs.usfca.edu/~cs272/birds/dove.html"),
				new URL("https://www.cs.usfca.edu/~cs272/birds/duck.html"),
				new URL("https://www.cs.usfca.edu/~cs272/birds/eagle.html"),
				new URL("https://www.cs.usfca.edu/~cs272/birds/egret.html"),
				new URL("https://www.cs.usfca.edu/~cs272/birds/falcon.html"),
				new URL("https://www.cs.usfca.edu/~cs272/birds/finch.html"),
				new URL("https://www.cs.usfca.edu/~cs272/birds/goose.html"),
				new URL("https://www.cs.usfca.edu/~cs272/birds/gull.html"),
				new URL("https://www.cs.usfca.edu/~cs272/birds/hawk.html"),
				new URL("https://www.cs.usfca.edu/~cs272/birds/heron.html"),
				new URL("https://www.cs.usfca.edu/~cs272/birds/hummingbird.html"),
				new URL("https://www.cs.usfca.edu/~cs272/birds/ibis.html"),
				new URL("https://www.cs.usfca.edu/~cs272/birds/kingfisher.html"),
				new URL("https://www.cs.usfca.edu/~cs272/birds/loon.html"),
				new URL("https://www.cs.usfca.edu/~cs272/birds/magpie.html"),
				new URL("https://www.cs.usfca.edu/~cs272/birds/mallard.html"),
				new URL("https://www.cs.usfca.edu/~cs272/birds/meadowlark.html"),
				new URL("https://www.cs.usfca.edu/~cs272/birds/mockingbird.html"),
				new URL("https://www.cs.usfca.edu/~cs272/birds/nighthawk.html"),
				new URL("https://www.cs.usfca.edu/~cs272/birds/osprey.html"),
				new URL("https://www.cs.usfca.edu/~cs272/birds/owl.html"),
				new URL("https://www.cs.usfca.edu/~cs272/birds/pelican.html"),
				new URL("https://www.cs.usfca.edu/~cs272/birds/pheasant.html"),
				new URL("https://www.cs.usfca.edu/~cs272/birds/pigeon.html"),
				new URL("https://www.cs.usfca.edu/~cs272/birds/puffin.html"),
				new URL("https://www.cs.usfca.edu/~cs272/birds/quail.html"),
				new URL("https://www.cs.usfca.edu/~cs272/birds/raven.html"),
				new URL("https://www.cs.usfca.edu/~cs272/birds/roadrunner.html"),
				new URL("https://www.cs.usfca.edu/~cs272/birds/robin.html"),
				new URL("https://www.cs.usfca.edu/~cs272/birds/sandpiper.html"),
				new URL("https://www.cs.usfca.edu/~cs272/birds/sparrow.html"),
				new URL("https://www.cs.usfca.edu/~cs272/birds/starling.html"),
				new URL("https://www.cs.usfca.edu/~cs272/birds/stork.html"),
				new URL("https://www.cs.usfca.edu/~cs272/birds/swallow.html"),
				new URL("https://www.cs.usfca.edu/~cs272/birds/swan.html"),
				new URL("https://www.cs.usfca.edu/~cs272/birds/tern.html"),
				new URL("https://www.cs.usfca.edu/~cs272/birds/turkey.html"),
				new URL("https://www.cs.usfca.edu/~cs272/birds/vulture.html"),
				new URL("https://www.cs.usfca.edu/~cs272/birds/warbler.html"),
				new URL("https://www.cs.usfca.edu/~cs272/birds/woodpecker.html"),
				new URL("https://www.cs.usfca.edu/~cs272/birds/wren.html"),
				new URL("https://www.cs.usfca.edu/~cs272/birds/yellowthroat.html"),
				new URL("https://www.cs.usfca.edu/~cs272/birds/birds.html"));
	}

	/**
	 * Helper method to get the expected URLs from a remote webpage.
	 *
	 * @return list of expected URLs
	 * @throws MalformedURLException if unable to create URLs
	 */
	public static List<URL> getGutenURLs() throws MalformedURLException {
		return List.of(
				new URL("https://www.cs.usfca.edu/~cs272/guten/1400-h/1400-h.htm"),
				new URL("https://www.cs.usfca.edu/~cs272/guten/50468-h/50468-h.htm"),
				new URL("https://www.cs.usfca.edu/~cs272/guten/2701-h/2701-h.htm"),
				new URL("https://www.cs.usfca.edu/~cs272/guten/1322-h/1322-h.htm"),
				new URL("https://www.cs.usfca.edu/~cs272/guten/1661-h/1661-h.htm"),
				new URL("https://www.cs.usfca.edu/~cs272/guten/22577-h/22577-h.htm"),
				new URL("https://www.cs.usfca.edu/~cs272/guten/37134-h/37134-h.htm"));
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
}
