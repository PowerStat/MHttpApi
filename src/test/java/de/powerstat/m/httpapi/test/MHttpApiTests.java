/*
 * Copyright (C) 2019-2024 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.m.httpapi.test;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import de.powerstat.m.httpapi.MHttpApi;
import de.powerstat.m.httpapi.MHttpApi.EventParams;
import de.powerstat.m.httpapi.MHttpApi.FaststreamParams;
import de.powerstat.m.httpapi.MHttpApi.FaststreamParams.Stream;
import de.powerstat.m.httpapi.MHttpApi.ImageParams;
import de.powerstat.m.httpapi.MHttpApi.ImageParams.Automatic;
import de.powerstat.m.httpapi.MHttpApi.ImageParams.Camera;
import de.powerstat.m.httpapi.MHttpApi.ImageParams.DisplayMode;
import de.powerstat.m.httpapi.MHttpApi.ImageParams.Error;
import de.powerstat.m.httpapi.MHttpApi.ImageParams.Mirror;
import de.powerstat.m.httpapi.MHttpApi.ImageParams.NoiseSuppress;
import de.powerstat.m.httpapi.MHttpApi.ImageParams.PipPosition;
import de.powerstat.m.httpapi.MHttpApi.ImageParams.TextDisplay;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * AHASessionMini tests.
 */
@SuppressWarnings("AvoidStaticImport")
@SuppressFBWarnings({"EC_NULL_ARG", "RV_NEGATING_RESULT_OF_COMPARETO", "SPP_USE_ZERO_WITH_COMPARATOR"})
final class MHttpApiTests
 {
  /**
   * Logger.
   */
  private static final Logger LOGGER = LogManager.getLogger(MHttpApiTests.class);

  /**
   * M test hostname.
   */
  private static final String MHOSTNAME = "192.168.1.0"; //$NON-NLS-1$

  /**
   * M test username.
   */
  private static final String MUSERNAME = "myusername"; //$NON-NLS-1$

  /**
   * M test password.
   */
  private static final String MPASSWORD = "mypassword"; //$NON-NLS-1$

  /**
   * Username.
   */
  private static final String USERNAME = "username"; //$NON-NLS-1$

  /**
   * Password.
   */
  private static final String TOP_SECRET = "TopSecret"; //$NON-NLS-1$

  /**
   * Action not as expected message.
   */
  private static final String ACTION_NOT_AS_EXPECTED = "Action not as expected"; //$NON-NLS-1$

  /**
   * Size parameter.
   */
  private static final String SIZE = "size=640x480"; //$NON-NLS-1$

  /**
   * No image text.
   */
  private static final String NO_IMAGE = "No image"; //$NON-NLS-1$

  /**
   * Height text.
   */
  private static final String HEIGHT = "; height: "; //$NON-NLS-1$

  /**
   * Width text.
   */
  private static final String WIDTH = "width: "; //$NON-NLS-1$

  /**
   * Jpg text.
   */
  private static final String JPG = "jpg"; //$NON-NLS-1$

  /**
   * Get url parameters not as expected text.
   */
  private static final String GET_URL_PARAMS_NOT_AS_EXPECTED = "getUrlParams not as expected"; //$NON-NLS-1$

  /**
   * Illegal argument exception expected constant.
   */
  private static final String ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED = "Illegal argument exception expected"; //$NON-NLS-1$


  /**
   * Default constructor.
   */
  /* default */ MHttpApiTests()
   {
    super();
   }


  /**
   * Test newInstance.
   *
   * @throws KeyManagementException KeyManagementException
   * @throws NoSuchAlgorithmException NoSuchAlgorithmException
   * @throws KeyStoreException KeyStoreException
   * @throws ParserConfigurationException ParserConfigurationException
   */
  @Test
  /* default */ void testNewInstance1() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, ParserConfigurationException
   {
    final MHttpApi mapi = MHttpApi.newInstance(MHttpApiTests.MHOSTNAME, 443, MHttpApiTests.MUSERNAME, MHttpApiTests.MPASSWORD);
    assertNotNull(mapi, "newInstance failed!"); //$NON-NLS-1$
   }


  /**
   * Test newInstance.
   */
  @Test
  /* default */ void testNewInstance2()
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final DocumentBuilder mockDocBuilder = mock(DocumentBuilder.class);
    final MHttpApi mapi = MHttpApi.newInstance(mockHttpclient, mockDocBuilder, MHttpApiTests.MHOSTNAME, 443);
    assertNotNull(mapi, "newInstance failed!"); //$NON-NLS-1$
   }


  /**
   * Test toString.
   */
  @Test
  /* default */ void testToString()
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final DocumentBuilder mockDocBuilder = mock(DocumentBuilder.class);
    final MHttpApi mapi = MHttpApi.newInstance(mockHttpclient, mockDocBuilder, MHttpApiTests.MHOSTNAME, 443);
    final String representation = mapi.toString();
    assertEquals("MHttpApi[hostname=" + MHttpApiTests.MHOSTNAME + "]", representation, "toString with unexpected result"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test getCurrentImage.
   *
   * @throws KeyManagementException Key management exception
   * @throws NoSuchAlgorithmException No such Algrithm exception
   * @throws KeyStoreException Key store exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws IOException IO exception
   *
   * TODO Mock
   */
  @Test
  /* default */ void testGetCurrentImage() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, ParserConfigurationException, IOException
   {
    final MHttpApi mapi = MHttpApi.newInstance(MHttpApiTests.MHOSTNAME, 443, MHttpApiTests.MUSERNAME, MHttpApiTests.MPASSWORD);
    final BufferedImage image = mapi.getCurrentImage();
    assertNotNull(image, MHttpApiTests.NO_IMAGE);
    MHttpApiTests.LOGGER.debug(MHttpApiTests.WIDTH + image.getWidth() + MHttpApiTests.HEIGHT + image.getHeight());
    ImageIO.write(image, MHttpApiTests.JPG, new File("target/(testCurrentImage.jpg")); //$NON-NLS-1$
   }


  /**
   * Test getImageCurrent.
   *
   * @throws KeyManagementException Key management exception
   * @throws NoSuchAlgorithmException No such Algrithm exception
   * @throws KeyStoreException Key store exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws IOException IO exception
   *
   * TODO Mock
   */
  @Test
  /* default */ void testGetImageCurrent() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, ParserConfigurationException, IOException
   {
    final MHttpApi mapi = MHttpApi.newInstance(MHttpApiTests.MHOSTNAME, 443, MHttpApiTests.MUSERNAME, MHttpApiTests.MPASSWORD);
    final BufferedImage image = mapi.getImageCurrent();
    assertNotNull(image, MHttpApiTests.NO_IMAGE);
    MHttpApiTests.LOGGER.debug(MHttpApiTests.WIDTH + image.getWidth() + MHttpApiTests.HEIGHT + image.getHeight());
    ImageIO.write(image, MHttpApiTests.JPG, new File("target/testImageCurrent.jpg")); //$NON-NLS-1$
   }


  /**
   * Test getImagePreview.
   *
   * @param width Image width
   * @throws KeyManagementException Key management exception
   * @throws NoSuchAlgorithmException No such Algrithm exception
   * @throws KeyStoreException Key store exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws IOException IO exception
   *
   * TODO Mock
   */
  @ParameterizedTest
  @ValueSource(ints = {0, 320, 640, 1280, 2048})
  /* default */ void testGetImagePreviewSuccess(final int width) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, ParserConfigurationException, IOException
   {
    final int height = (width / 4) * 3;
    final MHttpApi mapi = MHttpApi.newInstance(MHttpApiTests.MHOSTNAME, 443, MHttpApiTests.MUSERNAME, MHttpApiTests.MPASSWORD);
    final BufferedImage image = mapi.getImagePreview(width, height);
    assertNotNull(image, MHttpApiTests.NO_IMAGE);
    MHttpApiTests.LOGGER.debug(MHttpApiTests.WIDTH + image.getWidth() + MHttpApiTests.HEIGHT + image.getHeight());
    ImageIO.write(image, MHttpApiTests.JPG, new File("target/testImagePreview" + width + "x" + height + ".jpg")); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test getImagePreview with failure.
   *
   * @param width Image width
   * @throws ParserConfigurationException  Parser configuration exception
   * @throws KeyStoreException  Key store exception
   * @throws NoSuchAlgorithmException  No such algorithm exception
   * @throws KeyManagementException  Key management exception
   */
  @ParameterizedTest
  @ValueSource(ints = {-1, 2049})
  /* default */ void testGetImagePreviewFailure1(final int width) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, ParserConfigurationException
   {
    final int height = (width / 4) * 3;
    final MHttpApi mapi = MHttpApi.newInstance(MHttpApiTests.MHOSTNAME, 443, MHttpApiTests.MUSERNAME, MHttpApiTests.MPASSWORD);
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final BufferedImage image = */ mapi.getImagePreview(width, height);
     }, ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test getImagePreview with failure.
   *
   * @param height Image height
   * @throws ParserConfigurationException  Parser configuration exception
   * @throws KeyStoreException  Key store exception
   * @throws NoSuchAlgorithmException  No such algorithm exception
   * @throws KeyManagementException  Key management exception
   */
  @ParameterizedTest
  @ValueSource(ints = {-1, 2049})
  /* default */ void testGetImagePreviewFailure2(final int height) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, ParserConfigurationException
   {
    final MHttpApi mapi = MHttpApi.newInstance(MHttpApiTests.MHOSTNAME, 443, MHttpApiTests.MUSERNAME, MHttpApiTests.MPASSWORD);
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final BufferedImage image = */ mapi.getImagePreview(320, height);
     }, ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test getImageConfig.
   *
   * @throws KeyManagementException Key management exception
   * @throws NoSuchAlgorithmException No such Algrithm exception
   * @throws KeyStoreException Key store exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws IOException IO exception
   *
   * TODO Mock
   */
  @Test
  /* default */ void testGetImageConfig() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, ParserConfigurationException, IOException
   {
    final MHttpApi mapi = MHttpApi.newInstance(MHttpApiTests.MHOSTNAME, 443, MHttpApiTests.MUSERNAME, MHttpApiTests.MPASSWORD);
    final ImageParams params = new ImageParams();
    final BufferedImage image = mapi.getImageConfig(params);
    assertNotNull(image, MHttpApiTests.NO_IMAGE);
    MHttpApiTests.LOGGER.debug(MHttpApiTests.WIDTH + image.getWidth() + MHttpApiTests.HEIGHT + image.getHeight());
    ImageIO.write(image, MHttpApiTests.JPG, new File("target/testImageConfig.jpg")); //$NON-NLS-1$
   }


  /**
   * Test getImageProfile.
   *
   * @throws KeyManagementException Key management exception
   * @throws NoSuchAlgorithmException No such Algrithm exception
   * @throws KeyStoreException Key store exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws IOException IO exception
   *
   * TODO Mock
   */
  @Test
  /* default */ void testGetImageProfile() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, ParserConfigurationException, IOException
   {
    final MHttpApi mapi = MHttpApi.newInstance(MHttpApiTests.MHOSTNAME, 443, MHttpApiTests.MUSERNAME, MHttpApiTests.MPASSWORD);
    final ImageParams params = new ImageParams();
    final BufferedImage image = mapi.getImageProfile("", params);
    assertNotNull(image, MHttpApiTests.NO_IMAGE);
    MHttpApiTests.LOGGER.debug(MHttpApiTests.WIDTH + image.getWidth() + MHttpApiTests.HEIGHT + image.getHeight());
    ImageIO.write(image, MHttpApiTests.JPG, new File("target/testImageProfile.jpg")); //$NON-NLS-1$
   }


  /**
   * Test getImageView.
   *
   * @param view View number (-1..256)
   * @throws KeyManagementException Key management exception
   * @throws NoSuchAlgorithmException No such Algrithm exception
   * @throws KeyStoreException Key store exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws IOException IO exception
   *
   * TODO Mock
   */
  @ParameterizedTest
  @ValueSource(ints = {-1, 256})
  /* default */ void testImageView(final int view) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, ParserConfigurationException, IOException
   {
    final MHttpApi mapi = MHttpApi.newInstance(MHttpApiTests.MHOSTNAME, 443, MHttpApiTests.MUSERNAME, MHttpApiTests.MPASSWORD);
    final ImageParams params = new ImageParams();
    final BufferedImage image = mapi.getImageView(view, params);
    assertNotNull(image, MHttpApiTests.NO_IMAGE);
    MHttpApiTests.LOGGER.debug(MHttpApiTests.WIDTH + image.getWidth() + MHttpApiTests.HEIGHT + image.getHeight());
    ImageIO.write(image, MHttpApiTests.JPG, new File("target/testImageProfile.jpg")); //$NON-NLS-1$
   }


  /**
   * Test getImageView with failure.
   *
   * @param view View number
   * @throws ParserConfigurationException  Parser configuration exception
   * @throws KeyStoreException  Key store exception
   * @throws NoSuchAlgorithmException  No such algorithm exception
   * @throws KeyManagementException  Key management exception
   */
  @ParameterizedTest
  @ValueSource(ints = {-2, 257})
  /* default */ void testGetImageViewFailure1(final int view) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, ParserConfigurationException
   {
    final MHttpApi mapi = MHttpApi.newInstance(MHttpApiTests.MHOSTNAME, 443, MHttpApiTests.MUSERNAME, MHttpApiTests.MPASSWORD);
    final ImageParams params = new ImageParams();
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final BufferedImage image = */ mapi.getImageView(view, params);
     }, ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test getFaststream with no guest.
   *
   * @throws IOException IO exception
   * @throws KeyManagementException Key management exception
   * @throws NoSuchAlgorithmException No such algorithm
   * @throws KeyStoreException Key store exception
   * @throws ParserConfigurationException Parser configuration exception
   */
  @Test
  @Disabled("TODO")
  /* default */ void testGetFaststreamNoGuest() throws IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, ParserConfigurationException
   {
    final MHttpApi mapi = MHttpApi.newInstance(MHttpApiTests.MHOSTNAME, 443, MHttpApiTests.MUSERNAME, MHttpApiTests.MPASSWORD);
    final FaststreamParams params = new FaststreamParams();
    final BufferedImage image = mapi.getFaststream(false, params);
    assertNotNull(image, MHttpApiTests.NO_IMAGE);
    MHttpApiTests.LOGGER.debug(MHttpApiTests.WIDTH + image.getWidth() + MHttpApiTests.HEIGHT + image.getHeight());
    ImageIO.write(image, MHttpApiTests.JPG, new File("target/FaststreamNoGuest.jpg")); //$NON-NLS-1$
   }


  /**
   * Test getFaststream with guest.
   *
   * @throws IOException IO exception
   * @throws KeyManagementException Key management exception
   * @throws NoSuchAlgorithmException No such algorithm
   * @throws KeyStoreException Key store exception
   * @throws ParserConfigurationException Parser configuration exception
   */
  @Test
  @Disabled("TODO")
  /* default */ void testGetFaststreamGuest() throws IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, ParserConfigurationException
   {
    final MHttpApi mapi = MHttpApi.newInstance(MHttpApiTests.MHOSTNAME, 443, MHttpApiTests.MUSERNAME, MHttpApiTests.MPASSWORD);
    final FaststreamParams params = new FaststreamParams();
    final BufferedImage image = mapi.getFaststream(true, params);
    assertNotNull(image, MHttpApiTests.NO_IMAGE);
    MHttpApiTests.LOGGER.debug(MHttpApiTests.WIDTH + image.getWidth() + MHttpApiTests.HEIGHT + image.getHeight());
    ImageIO.write(image, MHttpApiTests.JPG, new File("target/FaststreamGuest.jpg")); //$NON-NLS-1$
   }


  /**
   * Test getEvent.
   *
   * @throws IOException IO exception
   * @throws KeyManagementException Key management exception
   * @throws NoSuchAlgorithmException No such algorithm
   * @throws KeyStoreException Key store exception
   * @throws ParserConfigurationException Parser configuration exception
   */
  @Test
  @Disabled("TODO")
  /* default */ void testGetEvent() throws IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, ParserConfigurationException
   {
    final MHttpApi mapi = MHttpApi.newInstance(MHttpApiTests.MHOSTNAME, 443, MHttpApiTests.MUSERNAME, MHttpApiTests.MPASSWORD);
    final EventParams params = new EventParams();
    final BufferedImage image = mapi.getEvent(params);
    assertNotNull(image, MHttpApiTests.NO_IMAGE);
    MHttpApiTests.LOGGER.debug(MHttpApiTests.WIDTH + image.getWidth() + MHttpApiTests.HEIGHT + image.getHeight());
    ImageIO.write(image, MHttpApiTests.JPG, new File("target/Event.jpg")); //$NON-NLS-1$
   }


  /**
   * Test ImageParams build.
   */
  @Test
  /* default */ void testImageParamsBuild()
   {
    final ImageParams params = new ImageParams();
    assertEquals(params, params.build(), "params not equal"); //$NON-NLS-1$
   }


  /**
   * Test ImageParams getUrlParams.
   *
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @Test
  /* default */ void testImageParamsGetUrlParams() throws UnsupportedEncodingException
   {
    final ImageParams params = new ImageParams();
    assertEquals("", params.getUrlParams(), MHttpApiTests.GET_URL_PARAMS_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Test ImageParams size.
   *
   * @param width Width
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(ints = {0, 320, 640, 1280, 2048})
  /* default */ void testImageParamsSize(final int width) throws UnsupportedEncodingException
   {
    final int height = (width / 4) * 3;
    final ImageParams params = new ImageParams().size(width, height);
    assertEquals(width == 0 ? "" : ("size=" + width + "x" + height), params.getUrlParams(), MHttpApiTests.GET_URL_PARAMS_NOT_AS_EXPECTED); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test ImageParams size width failure.
   *
   * @param width Width
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(ints = {-1, 2049})
  /* default */ void testImageParamsSizeFailure1(final int width) throws UnsupportedEncodingException
   {
    final int height = (width / 4) * 3;
    final ImageParams ip = new ImageParams();
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final ImageParams params = */ ip.size(width, height);
     }, ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test ImageParams size width failure.
   *
   * @param height Height
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(ints = {-1, 1537})
  /* default */ void testImageParamsSizeFailure2(final int height) throws UnsupportedEncodingException
   {
    final ImageParams ip = new ImageParams();
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final ImageParams params = */ ip.size(320, height);
     }, ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test ImageParams customSize.
   *
   * @param width width
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(ints = {0, 320, 640, 1280, 2048})
  /* default */ void testImageParamsCustomSize(final int width) throws UnsupportedEncodingException
   {
    final int height = (width / 4) * 3;
    final ImageParams params = new ImageParams().size(640, 480).customSize(width, height);
    assertEquals(MHttpApiTests.SIZE + (width == 0 ? "" : ("&customsize=" + width + "x" + height)), params.getUrlParams(), MHttpApiTests.GET_URL_PARAMS_NOT_AS_EXPECTED); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
   }


  /**
   * Test ImageParams customsize width failure.
   *
   * @param width Width
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(ints = {-1, 2049})
  /* default */ void testImageParamsCustomSizeFailure1(final int width) throws UnsupportedEncodingException
   {
    final int height = (width / 4) * 3;
    final ImageParams ip = new ImageParams();
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final ImageParams params = */ ip.customSize(width, height);
     }, ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test ImageParams customsize width failure.
   *
   * @param height Height
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(ints = {-1, 1537})
  /* default */ void testImageParamsCustomSizeFailure2(final int height) throws UnsupportedEncodingException
   {
    final ImageParams ip = new ImageParams();
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final ImageParams params = */ ip.customSize(320, height);
     }, ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test ImageParams customSize.
   *
   * @param width width
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(ints = {0, 320, 640, 1280, 2048})
  /* default */ void testImageParamsCustomSize2(final int width) throws UnsupportedEncodingException
   {
    final int height = (width / 4) * 3;
    final ImageParams params = new ImageParams().customSize(width, height);
    assertEquals((width == 0 ? "" : ("customsize=" + width + "x" + height)), params.getUrlParams(), MHttpApiTests.GET_URL_PARAMS_NOT_AS_EXPECTED); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
   }


  /**
   * Test ImageParams brightness.
   *
   * @param brightness Brightness
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(ints = {-10, 10})
  /* default */ void testImageParamsBrightness(final int brightness) throws UnsupportedEncodingException
   {
    final ImageParams params = new ImageParams().size(640, 480).brightness(brightness);
    assertEquals("size=640x480&brightness=" + brightness, params.getUrlParams(), MHttpApiTests.GET_URL_PARAMS_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Test ImageParams brightness with failure.
   *
   * @param brightness Brightness
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(ints = {-11, 11})
  /* default */ void testImageParamsBrightnessFailure(final int brightness) throws UnsupportedEncodingException
   {
    final ImageParams ip = new ImageParams().size(640, 480);
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final ImageParams params = */ ip.brightness(brightness);
     }, ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test ImageParams backlight.
   *
   * @param backlight Backlight
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(ints = {-10, 10})
  /* default */ void testImageParamsBacklight(final int backlight) throws UnsupportedEncodingException
   {
    final ImageParams params = new ImageParams().size(640, 480).backlight(backlight);
    assertEquals("size=640x480&backlight=" + backlight, params.getUrlParams(), MHttpApiTests.GET_URL_PARAMS_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Test ImageParams backlight with failure.
   *
   * @param backlight Backlight
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(ints = {-11, 11})
  /* default */ void testImageParamsBacklightsFailure(final int backlight) throws UnsupportedEncodingException
   {
    final ImageParams ip = new ImageParams().size(640, 480);
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final ImageParams params = */ ip.backlight(backlight);
     }, ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test ImageParams color.
   *
   * @param color Color
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(ints = {-10, 10})
  /* default */ void testImageParamsColor(final int color) throws UnsupportedEncodingException
   {
    final ImageParams params = new ImageParams().size(640, 480).color(color);
    assertEquals("size=640x480&color=" + color, params.getUrlParams(), MHttpApiTests.GET_URL_PARAMS_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Test ImageParams color with failure.
   *
   * @param color Color
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(ints = {-11, 11})
  /* default */ void testImageParamsColorFailure(final int color) throws UnsupportedEncodingException
   {
    final ImageParams ip = new ImageParams().size(640, 480);
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final ImageParams params = */ ip.color(color);
     }, ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test ImageParams blue.
   *
   * @param blue Blue
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(ints = {-10, 10})
  /* default */ void testImageParamsBlue(final int blue) throws UnsupportedEncodingException
   {
    final ImageParams params = new ImageParams().size(640, 480).blue(blue);
    assertEquals("size=640x480&blue=" + blue, params.getUrlParams(), MHttpApiTests.GET_URL_PARAMS_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Test ImageParams blue with failure.
   *
   * @param blue Blue
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(ints = {-11, 11})
  /* default */ void testImageParamsBlueFailure(final int blue) throws UnsupportedEncodingException
   {
    final ImageParams ip = new ImageParams().size(640, 480);
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final ImageParams params = */ ip.blue(blue);
     }, ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test ImageParams red.
   *
   * @param red Red
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(ints = {-10, 10})
  /* default */ void testImageParamsRed(final int red) throws UnsupportedEncodingException
   {
    final ImageParams params = new ImageParams().size(640, 480).red(red);
    assertEquals("size=640x480&red=" + red, params.getUrlParams(), MHttpApiTests.GET_URL_PARAMS_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Test ImageParams red with failure.
   *
   * @param red Red
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(ints = {-11, 11})
  /* default */ void testImageParamsRedFailure(final int red) throws UnsupportedEncodingException
   {
    final ImageParams ip = new ImageParams().size(640, 480);
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final ImageParams params = */ ip.red(red);
     }, ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test ImageParams sharpen.
   *
   * @param sharpen Sharpen
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(ints = {0, 10})
  /* default */ void testImageParamsSharpen(final int sharpen) throws UnsupportedEncodingException
   {
    final ImageParams params = new ImageParams().size(640, 480).sharpen(sharpen);
    assertEquals("size=640x480&sharpen=" + sharpen, params.getUrlParams(), MHttpApiTests.GET_URL_PARAMS_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Test ImageParams sharpen with failure.
   *
   * @param sharpen Sharpen
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(ints = {-1, 11})
  /* default */ void testImageParamsSharpenFailure(final int sharpen) throws UnsupportedEncodingException
   {
    final ImageParams ip = new ImageParams().size(640, 480);
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final ImageParams params = */ ip.sharpen(sharpen);
     }, ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test ImageParams quality.
   *
   * @param quality Quality
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(ints = {10, 90})
  /* default */ void testImageParamsQuality(final int quality) throws UnsupportedEncodingException
   {
    final ImageParams params = new ImageParams().size(640, 480).quality(quality);
    assertEquals("size=640x480&quality=" + quality, params.getUrlParams(), MHttpApiTests.GET_URL_PARAMS_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Test ImageParams quality with failure.
   *
   * @param quality Quality
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(ints = {9, 91})
  /* default */ void testImageParamsQualityFailure(final int quality) throws UnsupportedEncodingException
   {
    final ImageParams ip = new ImageParams().size(640, 480);
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final ImageParams params = */ ip.quality(quality);
     }, ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test ImageParams rotate.
   *
   * @param rotate Rotate
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(ints = {0, 360})
  /* default */ void testImageParamsRotate(final int rotate) throws UnsupportedEncodingException
   {
    final ImageParams params = new ImageParams().size(640, 480).rotate(rotate);
    assertEquals("size=640x480&rotate=" + rotate, params.getUrlParams(), MHttpApiTests.GET_URL_PARAMS_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Test ImageParams rotate with failure.
   *
   * @param rotate Rotate
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(ints = {-1, 361})
  /* default */ void testImageParamsRotateFailure(final int rotate) throws UnsupportedEncodingException
   {
    final ImageParams ip = new ImageParams().size(640, 480);
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final ImageParams params = */ ip.rotate(rotate);
     }, ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test ImageParams textbgopacity.
   *
   * @param textbgopacity Text background opacity
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(ints = {0, 100})
  /* default */ void testImageParamsTextbgopacity(final int textbgopacity) throws UnsupportedEncodingException
   {
    final ImageParams params = new ImageParams().size(640, 480).textbgopacity(textbgopacity);
    assertEquals("size=640x480&textbgopacity=" + textbgopacity, params.getUrlParams(), MHttpApiTests.GET_URL_PARAMS_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Test ImageParams textbgopacity with failure.
   *
   * @param textbgopacity Text background opacity
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(ints = {-1, 101})
  /* default */ void testImageParamsTextbgopacityFailure(final int textbgopacity) throws UnsupportedEncodingException
   {
    final ImageParams ip = new ImageParams().size(640, 480);
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final ImageParams params = */ ip.textbgopacity(textbgopacity);
     }, ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test ImageParams displayfontsize.
   *
   * @param displayfontsize Display font size
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(ints = {8, 24})
  /* default */ void testImageParamsDisplayfontsize(final int displayfontsize) throws UnsupportedEncodingException
   {
    final ImageParams params = new ImageParams().size(640, 480).displayfontsize(displayfontsize);
    assertEquals("size=640x480&displayfontsize=" + displayfontsize, params.getUrlParams(), MHttpApiTests.GET_URL_PARAMS_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Test ImageParams displayfontsizte with failure.
   *
   * @param displayfontsize Display font size
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(ints = {7, 25})
  /* default */ void testImageParamsDisplayfontsizeFailure(final int displayfontsize) throws UnsupportedEncodingException
   {
    final ImageParams ip = new ImageParams().size(640, 480);
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final ImageParams params = */ ip.displayfontsize(displayfontsize);
     }, ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test ImageParams date.
   *
   * @param date Date
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(ints = {0, 7})
  /* default */ void testImageParamsDate(final int date) throws UnsupportedEncodingException
   {
    final ImageParams params = new ImageParams().size(640, 480).date(date);
    assertEquals("size=640x480&date=" + date, params.getUrlParams(), MHttpApiTests.GET_URL_PARAMS_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Test ImageParams date with failure.
   *
   * @param date Date
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(ints = {-1, 8})
  /* default */ void testImageParamsDateFailure(final int date) throws UnsupportedEncodingException
   {
    final ImageParams ip = new ImageParams().size(640, 480);
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final ImageParams params = */ ip.date(date);
     }, ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test ImageParams text color.
   *
   * @param textcolor Display font size
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(ints = {0x000000, 0xFFFFFF})
  /* default */ void testImageParamsTextcolor(final int textcolor) throws UnsupportedEncodingException
   {
    final ImageParams params = new ImageParams().size(640, 480).textcolor(textcolor);
    assertEquals("size=640x480&textcolor=" + String.format("0x%06X", textcolor), params.getUrlParams(), MHttpApiTests.GET_URL_PARAMS_NOT_AS_EXPECTED); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test ImageParams textcolorwith failure.
   *
   * @param textcolor Text color
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(ints = {-1, 0x1000000})
  /* default */ void testImageParamsTextcolorFailure(final int textcolor) throws UnsupportedEncodingException
   {
    final ImageParams ip = new ImageParams().size(640, 480);
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final ImageParams params = */ ip.textcolor(textcolor);
     }, ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test ImageParams text background color.
   *
   * @param textbgcolor Display font size
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(ints = {0x000000, 0xFFFFFF})
  /* default */ void testImageParamsTextbgcolor(final int textbgcolor) throws UnsupportedEncodingException
   {
    final ImageParams params = new ImageParams().size(640, 480).textbgcolor(textbgcolor);
    assertEquals("size=640x480&textbgcolor=" + String.format("0x%06X", textbgcolor), params.getUrlParams(), MHttpApiTests.GET_URL_PARAMS_NOT_AS_EXPECTED); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test ImageParams textbgcolorwith failure.
   *
   * @param textbgcolor Text background color
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(ints = {-1, 0x1000000})
  /* default */ void testImageParamsTextbgcolorFailure(final int textbgcolor) throws UnsupportedEncodingException
   {
    final ImageParams ip = new ImageParams().size(640, 480);
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final ImageParams params = */ ip.textbgcolor(textbgcolor);
     }, ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test ImageParams stitch mode.
   *
   * @param stitchmode Stitch mode
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  /* default */ void testImageParamsStitchmode(final boolean stitchmode) throws UnsupportedEncodingException
   {
    final ImageParams params = new ImageParams().size(640, 480).stitchmode(stitchmode);
    assertEquals("size=640x480&stitchmode=" + (stitchmode ? "on" : "off"), params.getUrlParams(), MHttpApiTests.GET_URL_PARAMS_NOT_AS_EXPECTED); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
   }


  /**
   * Test ImageParams double pip.
   *
   * @param doublepip Stitch mode
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  /* default */ void testImageParamsDoublepip(final boolean doublepip) throws UnsupportedEncodingException
   {
    final ImageParams params = new ImageParams().size(640, 480).doublePip(doublepip);
    assertEquals("size=640x480&double_pip=" + (doublepip ? "on" : "off"), params.getUrlParams(), MHttpApiTests.GET_URL_PARAMS_NOT_AS_EXPECTED); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
   }


  /**
   * Test ImageParams show logo.
   *
   * @param showlogo Stitch mode
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(strings = {"MXLOGO-320", "MXLOGO-640", "MXLOGO-1280"})
  /* default */ void testImageParamsShowlogo(final String showlogo) throws UnsupportedEncodingException
   {
    final ImageParams params = new ImageParams().size(640, 480).showlogo(showlogo);
    assertEquals("size=640x480&showlogo=" + showlogo, params.getUrlParams(), MHttpApiTests.GET_URL_PARAMS_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Test ImageParams showlogo with failure.
   *
   * @param showlogo Show logo
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(strings = {"ä"})
  /* default */ void testImageParamsShowlogoFailure(final String showlogo) throws UnsupportedEncodingException
   {
    final ImageParams ip = new ImageParams().size(640, 480);
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final ImageParams params = */ ip.showlogo(showlogo);
     }, ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test ImageParams print test.
   *
   * @param print Print test
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(strings = {"Test"})
  /* default */ void testImageParamsPrint(final String print) throws UnsupportedEncodingException
   {
    final ImageParams params = new ImageParams().size(640, 480).print(print);
    assertEquals("size=640x480&print=" + print, params.getUrlParams(), MHttpApiTests.GET_URL_PARAMS_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Test ImageParams print with failure.
   *
   * @param print Print
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(strings = {"ä"})
  /* default */ void testImageParamsPrintFailure(final String print) throws UnsupportedEncodingException
   {
    final ImageParams ip = new ImageParams().size(640, 480);
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final ImageParams params = */ ip.print(print);
     }, ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test ImageParams noise suppress.
   *
   * @param noisesuppress Noise suppress
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @EnumSource(NoiseSuppress.class)
  /* default */ void testImageParamsNoisesuppress(final NoiseSuppress noisesuppress) throws UnsupportedEncodingException
   {
    final ImageParams params = new ImageParams().size(640, 480).noisesuppress(noisesuppress);
    assertEquals("size=640x480&noisesuppress=" + noisesuppress.name().toLowerCase(Locale.getDefault()), params.getUrlParams(), MHttpApiTests.GET_URL_PARAMS_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Test ImageParams text display.
   *
   * @param textdisplay Text display
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @EnumSource(TextDisplay.class)
  /* default */ void testImageParamsTextdisplay(final TextDisplay textdisplay) throws UnsupportedEncodingException
   {
    final ImageParams params = new ImageParams().size(640, 480).textdisplay(textdisplay);
    assertEquals("size=640x480&textdisplay=" + textdisplay.name().toLowerCase(Locale.getDefault()), params.getUrlParams(), MHttpApiTests.GET_URL_PARAMS_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Test ImageParams camera.
   *
   * @param camera Text display
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @EnumSource(Camera.class)
  /* default */ void testImageParamsCamera(final Camera camera) throws UnsupportedEncodingException
   {
    final ImageParams params = new ImageParams().size(640, 480).camera(camera);
    assertEquals("size=640x480&camera=" + camera.name().toLowerCase(Locale.getDefault()), params.getUrlParams(), MHttpApiTests.GET_URL_PARAMS_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Test ImageParams automatic.
   *
   * @param automatic automatic
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @EnumSource(Automatic.class)
  /* default */ void testImageParamsAutomatic(final Automatic automatic) throws UnsupportedEncodingException
   {
    final ImageParams params = new ImageParams().size(640, 480).automatic(automatic);
    assertEquals("size=640x480&automatic=" + automatic.name().toLowerCase(Locale.getDefault()), params.getUrlParams(), MHttpApiTests.GET_URL_PARAMS_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Test ImageParams mirror.
   *
   * @param mirror Mirror
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @EnumSource(Mirror.class)
  /* default */ void testImageParamsMirror(final Mirror mirror) throws UnsupportedEncodingException
   {
    final ImageParams params = new ImageParams().size(640, 480).mirror(mirror);
    assertEquals("size=640x480&mirror=" + mirror.name().toLowerCase(Locale.getDefault()), params.getUrlParams(), MHttpApiTests.GET_URL_PARAMS_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Test ImageParams error.
   *
   * @param error Error
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @EnumSource(Error.class)
  /* default */ void testImageParamsError(final Error error) throws UnsupportedEncodingException
   {
    final ImageParams params = new ImageParams().size(640, 480).error(error);
    assertEquals("size=640x480&error=" + error.name().toLowerCase(Locale.getDefault()), params.getUrlParams(), MHttpApiTests.GET_URL_PARAMS_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Test ImageParams pip position.
   *
   * @param pipposition Picture in picture position
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @EnumSource(PipPosition.class)
  /* default */ void testImageParamsPipposition(final PipPosition pipposition) throws UnsupportedEncodingException
   {
    final ImageParams params = new ImageParams().size(640, 480).pipposition(pipposition);
    assertEquals("size=640x480&pipposition=" + pipposition.name().toLowerCase(Locale.getDefault()), params.getUrlParams(), MHttpApiTests.GET_URL_PARAMS_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Test ImageParams display mode.
   *
   * @param displaymode Display mode
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @EnumSource(DisplayMode.class)
  /* default */ void testImageParamsDisplaymode(final DisplayMode displaymode) throws UnsupportedEncodingException
   {
    final ImageParams params = new ImageParams().size(640, 480).displaymode(displaymode);
    assertEquals("size=640x480&displaymode=" + displaymode.name().toLowerCase(Locale.getDefault()), params.getUrlParams(), MHttpApiTests.GET_URL_PARAMS_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Test NoiseSuppress getAction.
   */
  @Test
  /* default */ void testImageParamsNoiseSuppressGetAction()
   {
    assertEquals(2, ImageParams.NoiseSuppress.HIGH.getAction(), MHttpApiTests.ACTION_NOT_AS_EXPECTED);
   }


  /**
   * Test TextDisplay getAction.
   */
  @Test
  /* default */ void testImageParamsTextDisplayGetAction()
   {
    assertEquals(2, ImageParams.TextDisplay.DATETIME.getAction(), MHttpApiTests.ACTION_NOT_AS_EXPECTED);
   }


  /**
   * Test Camera getAction.
   */
  @Test
  /* default */ void testImageParamsCameraGetAction()
   {
    assertEquals(3, ImageParams.Camera.LIVE.getAction(), MHttpApiTests.ACTION_NOT_AS_EXPECTED);
   }


  /**
   * Test Automatic getAction.
   */
  @Test
  /* default */ void testImageParamsAutomaticGetAction()
   {
    assertEquals(3, ImageParams.Automatic.AGGRESSIVE.getAction(), MHttpApiTests.ACTION_NOT_AS_EXPECTED);
   }


  /**
   * Test Mirror getAction.
   */
  @Test
  /* default */ void testImageParamsMirrorGetAction()
   {
    assertEquals(3, ImageParams.Mirror.BOTH.getAction(), MHttpApiTests.ACTION_NOT_AS_EXPECTED);
   }


  /**
   * Test Error getAction.
   */
  @Test
  /* default */ void testImageParamsErrorGetAction()
   {
    assertEquals(3, ImageParams.Error.CURRENT.getAction(), MHttpApiTests.ACTION_NOT_AS_EXPECTED);
   }


  /**
   * Test PipPosition getAction.
   */
  @Test
  /* default */ void testImageParamsPipPositionGetAction()
   {
    assertEquals(3, ImageParams.PipPosition.TOP_LEFT.getAction(), MHttpApiTests.ACTION_NOT_AS_EXPECTED);
   }


  /**
   * Test DisplayMode getAction.
   */
  @Test
  /* default */ void testImageParamsDisplayModeGetAction()
   {
    assertEquals(8, ImageParams.DisplayMode.PIZ.getAction(), MHttpApiTests.ACTION_NOT_AS_EXPECTED);
   }


  /**
   * Test FaststreamParams build.
   */
  @Test
  /* default */ void testFaststreamParamsBuild()
   {
    final FaststreamParams params = new FaststreamParams();
    assertEquals(params, params.build(), "params not equal"); //$NON-NLS-1$
   }


  /**
   * Test FaststreamParams getUrlParams.
   *
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @Test
  /* default */ void testFaststreamParamsGetUrlParams() throws UnsupportedEncodingException
   {
    final FaststreamParams params = new FaststreamParams();
    assertEquals("", params.getUrlParams(), MHttpApiTests.GET_URL_PARAMS_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Test FaststreamParams size.
   *
   * @param width Width
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(ints = {0, 320, 640, 1280, 2048})
  /* default */ void testFaststreamParamsSize(final int width) throws UnsupportedEncodingException
   {
    final int height = (width / 4) * 3;
    final FaststreamParams params = new FaststreamParams().size(width, height);
    assertEquals(width == 0 ? "" : ("size=" + width + "x" + height), params.getUrlParams(), MHttpApiTests.GET_URL_PARAMS_NOT_AS_EXPECTED); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
   }


  /**
   * Test FaststreamParams size width failure.
   *
   * @param width Width
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(ints = {-1, 2049})
  /* default */ void testFaststreamParamsSizeFailure1(final int width) throws UnsupportedEncodingException
   {
    final int height = (width / 4) * 3;
    final FaststreamParams fsp = new FaststreamParams();
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final FaststreamParams params = */ fsp.size(width, height);
     }, ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test FaststreamParams size width failure.
   *
   * @param height Height
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(ints = {-1, 1537})
  /* default */ void testFaststreamParamsSizeFailure2(final int height) throws UnsupportedEncodingException
   {
    final FaststreamParams fsp = new FaststreamParams();
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final FaststreamParams params = */ fsp.size(320, height);
     }, ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test FaststreamParams iframerefresh.
   *
   * @param refresh Refresh
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(ints = {0, 60})
  /* default */ void testFaststreamParamsIframerefresh(final int refresh) throws UnsupportedEncodingException
   {
    final FaststreamParams params = new FaststreamParams().size(640, 480).iframerefresh(refresh);
    assertEquals("size=640x480&iframerefresh=" + refresh, params.getUrlParams(), MHttpApiTests.GET_URL_PARAMS_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Test FaststreamParams iframerefresh.
   *
   * @param refresh Refresh
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(ints = {0, 60})
  /* default */ void testFaststreamParamsIframerefresh2(final int refresh) throws UnsupportedEncodingException
   {
    final FaststreamParams params = new FaststreamParams().iframerefresh(refresh);
    assertEquals("iframerefresh=" + refresh, params.getUrlParams(), MHttpApiTests.GET_URL_PARAMS_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Test FaststreamParams iframerefresh.
   *
   * @param refresh iframe refresh
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(ints = {-1, 61})
  /* default */ void testFaststreamParamsIfraemrefreshFailure(final int refresh) throws UnsupportedEncodingException
   {
    final FaststreamParams fsp = new FaststreamParams();
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final FaststreamParams params = */ fsp.iframerefresh(refresh);
     }, ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test FaststreamParams jpheaderupdate.
   *
   * @param update Jpeg header update
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(ints = {0, 1000})
  /* default */ void testFaststreamParamsJpheaderupdate(final int update) throws UnsupportedEncodingException
   {
    final FaststreamParams params = new FaststreamParams().size(640, 480).jpheaderupdate(update);
    assertEquals("size=640x480&jpheaderupdate=" + update, params.getUrlParams(), MHttpApiTests.GET_URL_PARAMS_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Test FaststreamParams jpheaderupdare with failure.
   *
   * @param update jpeg header update
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(ints = {-1, 1001})
  /* default */ void testFaststreamParamsJpheaderupdateFailure(final int update) throws UnsupportedEncodingException
   {
    final FaststreamParams fsp = new FaststreamParams();
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final FaststreamParams params = */ fsp.jpheaderupdate(update);
     }, ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test FaststreamParams jpheaderrefresh.
   *
   * @param refresh Jpeg header refresh
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(ints = {0, 60})
  /* default */ void testFaststreamParamsJpheaderrefresh(final int refresh) throws UnsupportedEncodingException
   {
    final FaststreamParams params = new FaststreamParams().size(640, 480).jpheaderrefresh(refresh);
    assertEquals("size=640x480&jpheaderrefresh=" + refresh, params.getUrlParams(), MHttpApiTests.GET_URL_PARAMS_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Test FaststreamParams jpheaderrefresh with failure.
   *
   * @param refresh jpeg header refresh
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(ints = {-1, 61})
  /* default */ void testFaststreamParamsJpheaderrefreshFailure(final int refresh) throws UnsupportedEncodingException
   {
    final FaststreamParams fsp = new FaststreamParams();
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final FaststreamParams params = */ fsp.jpheaderrefresh(refresh);
     }, ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test FaststreamParams quality.
   *
   * @param quality Quality
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(ints = {10, 90})
  /* default */ void testFaststreamParamsQuality(final int quality) throws UnsupportedEncodingException
   {
    final FaststreamParams params = new FaststreamParams().size(640, 480).quality(quality);
    assertEquals("size=640x480&quality=" + quality, params.getUrlParams(), MHttpApiTests.GET_URL_PARAMS_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Test FaststreamParams quality with failure.
   *
   * @param quality Quality
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(ints = {9, 91})
  /* default */ void testFaststreamParamsQualityFailure(final int quality) throws UnsupportedEncodingException
   {
    final FaststreamParams fsp = new FaststreamParams();
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final FaststreamParams params = */ fsp.quality(quality);
     }, ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test FaststreamParams fps.
   *
   * @param fps Frames per second
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(ints = {0, 144})
  /* default */ void testFaststreamParamsFps(final int fps) throws UnsupportedEncodingException
   {
    final FaststreamParams params = new FaststreamParams().size(640, 480).fps(fps);
    assertEquals("size=640x480&fps=" + fps, params.getUrlParams(), MHttpApiTests.GET_URL_PARAMS_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Test FaststreamParams fps with failure.
   *
   * @param fps Fps
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(ints = {-1, 145})
  /* default */ void testFaststreamParamsFpsFailure(final int fps) throws UnsupportedEncodingException
   {
    final FaststreamParams fsp = new FaststreamParams();
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final FaststreamParams params = */ fsp.fps(fps);
     }, ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test FaststreamParams framecount.
   *
   * @param framecount Frame count
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(ints = {0, 144})
  /* default */ void testFaststreamParamsFramecount(final int framecount) throws UnsupportedEncodingException
   {
    final FaststreamParams params = new FaststreamParams().size(640, 480).framecount(framecount);
    assertEquals("size=640x480&framecount=" + framecount, params.getUrlParams(), MHttpApiTests.GET_URL_PARAMS_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Test FaststreamParams framecount with failure.
   *
   * @param framecount Frame count
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(ints = {-1})
  /* default */ void testFaststreamParamsFramecountFailure(final int framecount) throws UnsupportedEncodingException
   {
    final FaststreamParams fsp = new FaststreamParams();
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final FaststreamParams params = */ fsp.framecount(framecount);
     }, ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test FaststreamParams needlength.
   *
   * @param needlength Need content length
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(booleans = {true})
  /* default */ void testFaststreamParamsNeedlength(final boolean needlength) throws UnsupportedEncodingException
   {
    final FaststreamParams params = new FaststreamParams().size(640, 480).needlength(needlength);
    assertEquals(MHttpApiTests.SIZE + (needlength ? "&needlength" : ""), params.getUrlParams(), MHttpApiTests.GET_URL_PARAMS_NOT_AS_EXPECTED); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test FaststreamParams html.
   *
   * @param html Html output
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(booleans = {true})
  /* default */ void testFaststreamParamsHtml(final boolean html) throws UnsupportedEncodingException
   {
    final FaststreamParams params = new FaststreamParams().size(640, 480).html(html);
    assertEquals(MHttpApiTests.SIZE + (html ? "&html" : ""), params.getUrlParams(), MHttpApiTests.GET_URL_PARAMS_NOT_AS_EXPECTED); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test FaststreamParams noaudio.
   *
   * @param noaudio No audio output
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(booleans = {true})
  /* default */ void testFaststreamParamsNoaudio(final boolean noaudio) throws UnsupportedEncodingException
   {
    final FaststreamParams params = new FaststreamParams().size(640, 480).noaudio(noaudio);
    assertEquals(MHttpApiTests.SIZE + (noaudio ? "&noaudio" : ""), params.getUrlParams(), MHttpApiTests.GET_URL_PARAMS_NOT_AS_EXPECTED); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test FaststreamParams preview.
   *
   * @param preview Preview output
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(booleans = {true})
  /* default */ void testFaststreamParamsPreview(final boolean preview) throws UnsupportedEncodingException
   {
    final FaststreamParams params = new FaststreamParams().size(640, 480).preview(preview);
    assertEquals(MHttpApiTests.SIZE + (preview ? "&preview" : ""), params.getUrlParams(), MHttpApiTests.GET_URL_PARAMS_NOT_AS_EXPECTED); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test FaststreamParams error.
   *
   * @param error Error
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @EnumSource(FaststreamParams.Error.class)
  /* default */ void testFaststreamParamsError(final FaststreamParams.Error error) throws UnsupportedEncodingException
   {
    final FaststreamParams params = new FaststreamParams().size(640, 480).error(error);
    assertEquals("size=640x480&error=" + error.name().toLowerCase(Locale.getDefault()), params.getUrlParams(), MHttpApiTests.GET_URL_PARAMS_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Test FaststreamParams camera.
   *
   * @param camera Text display
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @EnumSource(FaststreamParams.Camera.class)
  /* default */ void testStreamParamsCamera(final FaststreamParams.Camera camera) throws UnsupportedEncodingException
   {
    final FaststreamParams params = new FaststreamParams().size(640, 480).camera(camera);
    assertEquals("size=640x480&camera=" + camera.name().toLowerCase(Locale.getDefault()), params.getUrlParams(), MHttpApiTests.GET_URL_PARAMS_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Test FaststreamParams stream.
   *
   * @param stream Stream type
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @EnumSource(Stream.class)
  /* default */ void testImageParamsStream(final Stream stream) throws UnsupportedEncodingException
   {
    final FaststreamParams params = new FaststreamParams().size(640, 480).stream(stream);
    assertEquals("size=640x480&stream=" + stream.name().toLowerCase(Locale.getDefault()), params.getUrlParams(), MHttpApiTests.GET_URL_PARAMS_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Test Error getAction.
   */
  @Test
  /* default */ void testFaststreamParamsErrorGetAction()
   {
    assertEquals(3, FaststreamParams.Error.CURRENT.getAction(), MHttpApiTests.ACTION_NOT_AS_EXPECTED);
   }


  /**
   * Test Camera getAction.
   */
  @Test
  /* default */ void testFaststreamParamsCameraGetAction()
   {
    assertEquals(2, FaststreamParams.Camera.BOTH.getAction(), MHttpApiTests.ACTION_NOT_AS_EXPECTED);
   }


  /**
   * Test Stream getAction.
   */
  @Test
  /* default */ void testFaststreamParamsStreamGetAction()
   {
    assertEquals(2, FaststreamParams.Stream.MXG.getAction(), MHttpApiTests.ACTION_NOT_AS_EXPECTED);
   }


  /**
   * Test EventParams build.
   */
  @Test
  /* default */ void testEventParamsBuild()
   {
    final EventParams params = new EventParams();
    assertEquals(params, params.build(), "params not equal"); //$NON-NLS-1$
   }


  /**
   * Test EventParams getUrlParams.
   *
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @Test
 /* default */ void testEventParamsGetUrlParams() throws UnsupportedEncodingException
   {
    final EventParams params = new EventParams();
    assertEquals("", params.getUrlParams(), MHttpApiTests.GET_URL_PARAMS_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Test EventParams size.
   *
   * @param width Width
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(ints = {0, 320, 640, 1280, 2048})
  /* default */ void testEventParamsSize(final int width) throws UnsupportedEncodingException
   {
    final int height = (width / 4) * 3;
    final EventParams params = new EventParams().size(width, height);
    assertEquals(width == 0 ? "" : ("size=" + width + "x" + height), params.getUrlParams(), MHttpApiTests.GET_URL_PARAMS_NOT_AS_EXPECTED); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
   }


  /**
   * Test EventParams size width failure.
   *
   * @param width Width
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(ints = {-1, 2049})
  /* default */ void testEventParamsSizeFailure1(final int width) throws UnsupportedEncodingException
   {
    final int height = (width / 4) * 3;
    final EventParams ep = new EventParams();
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final EventParams params = */ ep.size(width, height);
     }, ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test EventParams size width failure.
   *
   * @param height Height
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @ParameterizedTest
  @ValueSource(ints = {-1, 1537})
  /* default */ void testEventParamsSizeFailure2(final int height) throws UnsupportedEncodingException
   {
    final EventParams ep = new EventParams();
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final EventParams params = */ ep.size(320, height);
     }, ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test hash code.
   *
   * @throws ParserConfigurationException Parser configuration exception
   * @throws KeyStoreException  Key store exception
   * @throws NoSuchAlgorithmException  No such algorithm exception
   * @throws KeyManagementException  Key management exception
   */
  @Test
  /* default */ void testHashCode() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, ParserConfigurationException
   {
    final MHttpApi mapi1 = MHttpApi.newInstance(MHttpApiTests.MHOSTNAME, 443, MHttpApiTests.USERNAME, MHttpApiTests.TOP_SECRET);
    final MHttpApi mapi2 = MHttpApi.newInstance(MHttpApiTests.MHOSTNAME, 443, MHttpApiTests.USERNAME, MHttpApiTests.TOP_SECRET);
    final MHttpApi mapi3 = MHttpApi.newInstance("192.168.1.41", 443, MHttpApiTests.USERNAME, "TopSecret2"); //$NON-NLS-1$ //$NON-NLS-2$
    assertAll("testHashCode", //$NON-NLS-1$
      () -> assertEquals(mapi1.hashCode(), mapi2.hashCode(), "hashCodes are not equal"), //$NON-NLS-1$
      () -> assertNotEquals(mapi1.hashCode(), mapi3.hashCode(), "hashCodes are equal") //$NON-NLS-1$
    );
   }


  /**
   * Test equals.
   *
   * @throws ParserConfigurationException Parser configuration exception
   * @throws KeyStoreException  Key store exception
   * @throws NoSuchAlgorithmException  No such algorithm exception
   * @throws KeyManagementException  Key management exception
   */
  @Test
  @SuppressWarnings({"PMD.EqualsNull", "java:S5785"})
  /* default */ void testEquals() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, ParserConfigurationException
   {
    final MHttpApi mapi1 = MHttpApi.newInstance(MHttpApiTests.MHOSTNAME, 443, MHttpApiTests.USERNAME, MHttpApiTests.TOP_SECRET);
    final MHttpApi mapi2 = MHttpApi.newInstance(MHttpApiTests.MHOSTNAME, 443, MHttpApiTests.USERNAME, MHttpApiTests.TOP_SECRET);
    final MHttpApi mapi3 = MHttpApi.newInstance("192.168.1.41", 443, MHttpApiTests.USERNAME, "TopSecret2"); //$NON-NLS-1$ //$NON-NLS-2$
    final MHttpApi mapi4 = MHttpApi.newInstance(MHttpApiTests.MHOSTNAME, 443, MHttpApiTests.USERNAME, MHttpApiTests.TOP_SECRET);
    assertAll("testEquals", //$NON-NLS-1$
      () -> assertTrue(mapi1.equals(mapi1), "mapi11 is not equal"), //$NON-NLS-1$
      () -> assertTrue(mapi1.equals(mapi2), "mapi12 are not equal"), //$NON-NLS-1$
      () -> assertTrue(mapi2.equals(mapi1), "mapi21 are not equal"), //$NON-NLS-1$
      () -> assertTrue(mapi2.equals(mapi4), "mapi24 are not equal"), //$NON-NLS-1$
      () -> assertTrue(mapi1.equals(mapi4), "mapi14 are not equal"), //$NON-NLS-1$
      () -> assertFalse(mapi1.equals(mapi3), "mapi13 are equal"), //$NON-NLS-1$
      () -> assertFalse(mapi3.equals(mapi1), "mapi31 are equal"), //$NON-NLS-1$
      () -> assertFalse(mapi1.equals(null), "mapi10 is equal") //$NON-NLS-1$
    );
   }


  /**
   * Test compareTo.
   *
   * @throws ParserConfigurationException Parser configuration exception
   * @throws KeyStoreException  Key store exception
   * @throws NoSuchAlgorithmException  No such algorithm exception
   * @throws KeyManagementException  Key management exception
   */
  @Test
  @SuppressWarnings("java:S5785")
  /* default */ void testCompareTo() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, ParserConfigurationException
   {
    final MHttpApi mapi1 = MHttpApi.newInstance(MHttpApiTests.MHOSTNAME, 443, MHttpApiTests.USERNAME, MHttpApiTests.TOP_SECRET);
    final MHttpApi mapi2 = MHttpApi.newInstance(MHttpApiTests.MHOSTNAME, 443, MHttpApiTests.USERNAME, MHttpApiTests.TOP_SECRET);
    final MHttpApi mapi3 = MHttpApi.newInstance("192.168.1.41", 443, MHttpApiTests.USERNAME, "TopSecret2"); //$NON-NLS-1$ //$NON-NLS-2$
    final MHttpApi mapi4 = MHttpApi.newInstance("192.168.1.42", 443, MHttpApiTests.USERNAME, "TopSecret3"); //$NON-NLS-1$ //$NON-NLS-2$
    final MHttpApi mapi5 = MHttpApi.newInstance(MHttpApiTests.MHOSTNAME, 443, MHttpApiTests.USERNAME, MHttpApiTests.TOP_SECRET);
    final MHttpApi mapi6 = MHttpApi.newInstance(MHttpApiTests.MHOSTNAME, 443, "admin", MHttpApiTests.TOP_SECRET); //$NON-NLS-1$
    assertAll("testCompareTo", //$NON-NLS-1$
      () -> assertTrue(mapi1.compareTo(mapi2) == -mapi2.compareTo(mapi1), "reflexive1"), //$NON-NLS-1$
      () -> assertTrue(mapi1.compareTo(mapi3) == -mapi3.compareTo(mapi1), "reflexive2"), //$NON-NLS-1$
      () -> assertTrue((mapi4.compareTo(mapi3) > 0) && (mapi3.compareTo(mapi1) > 0) && (mapi4.compareTo(mapi1) > 0), "transitive1"), //$NON-NLS-1$
      () -> assertTrue((mapi1.compareTo(mapi2) == 0) && (Math.abs(mapi1.compareTo(mapi5)) == Math.abs(mapi2.compareTo(mapi5))), "sgn1"), //$NON-NLS-1$
      () -> assertTrue((mapi1.compareTo(mapi2) == 0) && mapi1.equals(mapi2), "equals"), //$NON-NLS-1$
      () -> assertTrue((mapi1.compareTo(mapi6) == 0), "not as expected") //$NON-NLS-1$
    );
   }

 }
