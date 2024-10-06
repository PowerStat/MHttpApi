/*
 * Copyright (C) 2015-2024 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.m.httpapi;


import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.powerstat.validation.ValidationUtils;
import de.powerstat.validation.values.Hostname;
import de.powerstat.validation.values.Password;
import de.powerstat.validation.values.Port;
import de.powerstat.validation.values.Username;


/**
 * M http api.
 *
 * This class is not serializable because of session management!
 *
 * @author Kai Hofmann
 * @see <a href="https://developer.mobotix.com/">Mobotix Camera HTTP API</a>
 * @see <a href="https://community.mobotix.com/t/erste-schritte-mit-der-http-api/529">Erste Schritte mit der HTTP-API</a>
 *
 * TODO version number handling?
 */
@SuppressWarnings({"java:S1160", "java:S1820"})
public final class MHttpApi implements Comparable<MHttpApi>
 {
  /**
   * Logger.
   */
  private static final Logger LOGGER = LogManager.getLogger(MHttpApi.class);

  /**
   * Wrong height message.
   */
  private static final String WRONG_HEIGHT = "Wrong height (0-1536)"; //$NON-NLS-1$

  /**
   * Wrong width message.
   */
  private static final String WRONG_WIDTH = "Wrong width (0-2048)"; //$NON-NLS-1$

  /**
   * Params text.
   */
  private static final String PARAMS = "params"; //$NON-NLS-1$

  /**
   * Logo name regexp.
   */
  private static final Pattern LOGONAME_REGEXP = Pattern.compile("^[A-Za-z0-9_-]+$"); //$NON-NLS-1$

  /**
   * Print content regexp.
   */
  private static final Pattern PRINTCONTENT_REGEXP = Pattern.compile("^[A-Za-z0-9_-]+$"); //$NON-NLS-1$

  /**
   * Size constant.
   */
  private static final String SIZE = "size=";

  /* *
   * Document builder.
   */
  // private final DocumentBuilder docBuilder;

  /**
   * HTTP client.
   */
  private final CloseableHttpClient httpclient;

  /**
   * M hostname.
   */
  private final Hostname hostname;

  /**
   * M port.
   */
  private final Port port;


  /**
   * Constructor.
   *
   * @param httpclient CloseableHttpClient
   * @param docBuilder DocumentBuilder
   * @param hostname M hostname
   * @param port M port
   * @throws NullPointerException If a parameter is null
   */
  protected MHttpApi(final CloseableHttpClient httpclient, final DocumentBuilder docBuilder, final Hostname hostname, final Port port)
   {
    super();
    this.hostname = Objects.requireNonNull(hostname, "hostname"); //$NON-NLS-1$
    this.port = Objects.requireNonNull(port, "port"); //$NON-NLS-1$
    this.httpclient = Objects.requireNonNull(httpclient, "httpclient"); //$NON-NLS-1$
    // this.docBuilder = Objects.requireNonNull(docBuilder, "docBuilder"); //$NON-NLS-1$
   }


  /**
   * Get new instance for a M hostname, port with httpclient and document builder.
   *
   * @param httpclient CloseableHttpClient
   * @param docBuilder DocumentBuilder
   * @param hostname M hostname
   * @param port M port
   * @return A new MHttpApi instance.
   * @throws NullPointerException If hostname or password is null
   */
  public static MHttpApi newInstance(final CloseableHttpClient httpclient, final DocumentBuilder docBuilder, final Hostname hostname, final Port port)
   {
    return new MHttpApi(httpclient, docBuilder, hostname, port);
   }


  /**
   * Get new instance for a M hostname, port with httpclient and document builder.
   *
   * @param httpclient CloseableHttpClient
   * @param docBuilder DocumentBuilder
   * @param hostname M hostname
   * @param port M port
   * @return A new MHttpApi instance.
   * @throws NullPointerException If a parameter is null
   */
  public static MHttpApi newInstance(final CloseableHttpClient httpclient, final DocumentBuilder docBuilder, final String hostname, final int port)
   {
    return MHttpApi.newInstance(httpclient, docBuilder, Hostname.of(hostname), Port.of(port));
   }


  /**
   * Get new instance for a M hostname, port with username and password.
   *
   * @param hostname M hostname
   * @param port M port
   * @param username M username
   * @param password M password
   * @return A new MHttpApi instance.
   * @throws ParserConfigurationException Parser configuration exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws KeyStoreException Key store exception
   * @throws KeyManagementException Key management exception
   * @throws NullPointerException If hostname or password is null
   */
  public static MHttpApi newInstance(final Hostname hostname, final Port port, final Username username, final Password password) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, ParserConfigurationException
   {
    final CredentialsProvider credsProvider = new BasicCredentialsProvider();
    credsProvider.setCredentials(new AuthScope(hostname.stringValue(), port.intValue()), new UsernamePasswordCredentials(username.stringValue(), password.stringValue()));
    final CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(new SSLConnectionSocketFactory(new SSLContextBuilder().loadTrustMaterial(null, new TrustAllStrategy()).build(), NoopHostnameVerifier.INSTANCE)).setDefaultCredentialsProvider(credsProvider).build();

    final var factory = DocumentBuilderFactory.newInstance();
    try
     {
      factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
      // factory.setFeature(XMLConstants.ACCESS_EXTERNAL_DTD, false);
      // factory.setFeature(XMLConstants.ACCESS_EXTERNAL_SCHEMA, false);
      // factory.setFeature(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, false);
      factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true); //$NON-NLS-1$
      // factory.setFeature("http://xml.org/sax/features/external-general-entities", true); //$NON-NLS-1$
      // factory.setFeature("http://xml.org/sax/features/external-parameter-entities", true); //$NON-NLS-1$
      // factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", true); //$NON-NLS-1$
      // factory.setXIncludeAware(false);
      // factory.setExpandEntityReferences(false);
      final var docBuilder = factory.newDocumentBuilder();
      return newInstance(httpclient, docBuilder, hostname, port);
     }
    catch (final ParserConfigurationException e)
     {
      try
       {
        httpclient.close();
       }
      catch (final IOException e1)
       {
        // ignore
       }
      throw e;
     }
   }


  /**
   * Get new instance for a M hostname, port with username and password.
   *
   * @param hostname M hostname
   * @param port M port
   * @param username M username
   * @param password M password
   * @return A new MHttpApi instance.
   * @throws ParserConfigurationException Parser configuration exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws KeyStoreException Key store exception
   * @throws KeyManagementException Key management exception
   * @throws NullPointerException If hostname or password is null
   */
  public static MHttpApi newInstance(final String hostname, final int port, final String username, final String password) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, ParserConfigurationException
   {
    return newInstance(Hostname.of(hostname), Port.of(port), Username.of(username), Password.of(password));
   }


  /**
   * Get image.
   *
   * @param urlPath Relative url path
   * @return BufferedImage
   * @throws IOException IO exception
   */
  private BufferedImage getImage(final String urlPath) throws IOException
   {
    assert urlPath != null;
    try (CloseableHttpResponse response = this.httpclient.execute(new HttpGet("https://" + this.hostname.stringValue() + ":" + this.port.intValue() + ValidationUtils.sanitizeUrlPath(urlPath)))) //$NON-NLS-1$ //$NON-NLS-2$
     {
      final int responseCode = response.getStatusLine().getStatusCode();
      if (responseCode != HttpURLConnection.HTTP_OK)
       {
        if (MHttpApi.LOGGER.isDebugEnabled())
         {
          MHttpApi.LOGGER.debug(response.getStatusLine());
         }
        if (MHttpApi.LOGGER.isInfoEnabled())
         {
          MHttpApi.LOGGER.info("HttpStatus: {}:", response.getStatusLine().getStatusCode());  //$NON-NLS-1$
         }
        if (responseCode == HttpURLConnection.HTTP_FORBIDDEN)
         {
          throw new IOException("Credentials failure!"); //$NON-NLS-1$
         }
        if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST)
         {
          throw new UnsupportedOperationException("Possibly you used a command from a newer api version?"); //$NON-NLS-1$
         }
       }
      final HttpEntity entity = response.getEntity();
      if (MHttpApi.LOGGER.isDebugEnabled())
       {
        MHttpApi.LOGGER.debug("ContentType:{} ", entity.getContentType()); //$NON-NLS-1$
       }
      return ImageIO.read(entity.getContent());
     }

   }


  /**
   * Get current image.
   *
   * @return BufferedImage
   * @throws IOException IO exception
   */
  public BufferedImage getCurrentImage() throws IOException
   {
    return getImage("/record/current.jpg"); //$NON-NLS-1$
   }


  /**
   * Get current image.
   *
   * @return Buffered image
   * @throws IOException IO exception
   */
  public BufferedImage getImageCurrent() throws IOException
   {
    return getImage("/cgi-bin/image.jpg?current"); //$NON-NLS-1$
   }


  /**
   * Get preview image.
   *
   * @param width Width (0-2048), must be 4:3 with height
   * @param height Height (0-1536), must be 4:3 with height
   * @return Buffered image
   * @throws IOException IO exception
   * @throws IllegalArgumentException If (width or height) < 0 or width > 2048 or height > 1536
   */
  public BufferedImage getImagePreview(final int width, final int height) throws IOException
   {
    if ((width < 0) || (width > 2048))
     {
      throw new IllegalArgumentException(MHttpApi.WRONG_WIDTH);
     }
    if ((height < 0) || (height > 1536))
     {
      throw new IllegalArgumentException(MHttpApi.WRONG_HEIGHT);
     }
    // width / 4 == height / 3
    return getImage("/cgi-bin/image.jpg?preview&size=" + width + "x" + height); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Get image by config.
   *
   * @param params Optional parameters
   * @return Buffered image
   * @throws IOException IO exception
   * @throws NullPointerException If params is null
   */
  public BufferedImage getImageConfig(final ImageParams params) throws IOException
   {
    Objects.requireNonNull(params, MHttpApi.PARAMS);
    return getImage("/cgi-bin/image.jpg?config&" + params.getUrlParams()); //$NON-NLS-1$
   }


  /**
   * Get image by profile.
   *
   * @param profileName Profile name (QXGA|MEGA|VGA|CIF|....)
   * @param params Optional parameters
   * @return Buffered image
   * @throws IOException IO exception
   * @throws NullPointerException If params is null
   */
  public BufferedImage getImageProfile(final String profileName, final ImageParams params) throws IOException
   {
    Objects.requireNonNull(params, MHttpApi.PARAMS);
    return getImage("/cgi-bin/image.jpg?imgprof=" + profileName + "&" + params.getUrlParams()); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Get image by view.
   *
   * @param viewNr View nr (-1..256)
   * @param params Optional parameters
   * @return Buffered image
   * @throws IOException IO exception
   * @throws IllegalArgumentException Illegal argument exception
   * @throws NullPointerException If params is null
   */
  public BufferedImage getImageView(final int viewNr, final ImageParams params) throws IOException
   {
    if ((viewNr < -1) || (viewNr > 256))
     {
      throw new IllegalArgumentException("viewNr must be -1..256"); //$NON-NLS-1$
     }
    Objects.requireNonNull(params, MHttpApi.PARAMS);
    return getImage("/cgi-bin/image.jpg?view=" + viewNr + "&" + params.getUrlParams()); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Get fast stream.
   *
   * @param guest Guest or user account
   * @param params Optional parameters
   * @return BufferedImage
   * @throws IOException IO exception
   *
   * TODO Handle differnt return value types: HTML, full, MxPEG, mxg
   */
  @SuppressWarnings("java:S2301")
  public BufferedImage getFaststream(final boolean guest, final FaststreamParams params) throws IOException
   {
    Objects.requireNonNull(params, MHttpApi.PARAMS);
    return getImage((guest ? "/cgi-bin" : "/control") + "/faststream.jpg"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
   }


  /**
   * Get event picture.
   *
   * @param params Optional parameters
   * @return BufferedImage
   * @throws IOException IO exception
   */
  public BufferedImage getEvent(final EventParams params) throws IOException
   {
    Objects.requireNonNull(params, MHttpApi.PARAMS);
    return getImage("/control/event.jpg"); //$NON-NLS-1$
   }


  /*
   http://192.168.1.40/control/event.jpg
  */


  /**
   * Calculate hash code.
   *
   * @return Hash
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
   {
    return Objects.hash(this.hostname);
   }


  /**
   * Is equal with another object.
   *
   * @param obj Object
   * @return true when equal, false otherwise
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(final Object obj)
   {
    if (this == obj)
     {
      return true;
     }
    if (!(obj instanceof final MHttpApi other))
     {
      return false;
     }
    return (this.hostname.equals(other.hostname));
   }


  /**
   * Returns the string representation of this AHASessionMini.
   *
   * The exact details of this representation are unspecified and subject to change, but the following may be regarded as typical:
   *
   * "AHASessionMini[hostname=fritz.box, username=, sid=000000000000, ...]"
   *
   * @return String representation of this AHASessionMini.
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
   {
    return new StringBuilder().append("MHttpApi[hostname=").append(this.hostname.stringValue()).append(']').toString(); //$NON-NLS-1$
   }


  /**
   * Compare with another object.
   *
   * @param obj Object to compare with
   * @return 0: equal; 1: greater; -1: smaller
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo(final MHttpApi obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    return this.hostname.compareTo(obj.hostname);
   }


  /**
   * Image parameter builder.
   */
  @SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
  public static class ImageParams
   {
    /**
     * Image size width.
     */
    private int width;

    /**
     * Image size height.
     */
    private int height;

    /**
     * Image custom size width.
     */
    private int custWidth;

    /**
     * Image custom size height.
     */
    private int custHeight;

    /**
     * Brightness.
     */
    private Integer brightness;

    /**
     * Backlight.
     */
    private Integer backlight;

    /**
     * Color saturation.
     */
    private Integer color;

    /**
     * Blue adjustment.
     */
    private Integer blue;

    /**
     * Red adjustment.
     */
    private Integer red;

    /**
     * Sharpen.
     */
    private Integer sharpen;

    /**
     * JPEG Quality.
     */
    private int quality;

    /**
     * Rotate.
     */
    private Integer rotate;

    /**
     * Text background opacity.
     */
    private Integer textBgOpacity;

    /**
     * Display font site.
     */
    private int displayFontSize;

    /**
     * Date type.
     */
    private Integer date;

    /**
     * Text color.
     */
    private Integer textcolor;

    /**
     * Text background color.
     */
    private Integer textbgcolor;

    /**
     * Stitch mode.
     */
    private Boolean stitchmode;

    /**
     * Double picture in picture.
     */
    private Boolean doublePip;

    /**
     * Show logo name.
     */
    private String showlogo;

    /**
     * Print.
     */
    private String print;

    /**
     * Noise suppress.
     */
    private NoiseSuppress noisesuppress;

    /**
     * Text display.
     */
    private TextDisplay textdisplay;

    /**
     * Camera.
     */
    private Camera camera;

    /**
     * Automatic image improvement.
     */
    private Automatic automatic;

    /**
     * Mirror image.
     */
    private Mirror mirror;

    /**
     * Error.
     */
    private Error error;

    /**
     * Picture in picture position.
     */
    private PipPosition pipposition;

    /**
     * Display mode.
     */
    private DisplayMode displayMode;


    /**
     * Enum for noise suppress.
     */
    public enum NoiseSuppress
     {
      /**
       * Noise suppress off.
       */
      OFF(0),

      /**
       * Noise suppress low.
       */
      LOW(1),

      /**
       * Noise suppress high.
       */
      HIGH(2);


      /**
       * Action number.
       */
      private final int action;


      /**
       * Ordinal constructor.
       *
       * @param action Action number
       */
      NoiseSuppress(final int action)
       {
        this.action = action;
       }


      /**
       * Get action number.
       *
       * @return Action number
       */
      public int getAction()
       {
        return this.action;
       }

     }


    /**
     * Enum for text display.
     */
    public enum TextDisplay
     {
      /**
       * disable text display.
       */
      DISABLE(0),

      /**
       * enable text display.
       */
      ENABLE(1),

      /**
       * Datetime display.
       */
      DATETIME(2);


      /**
       * Action number.
       */
      private final int action;


      /**
       * Ordinal constructor.
       *
       * @param action Action number
       */
      TextDisplay(final int action)
       {
        this.action = action;
       }


      /**
       * Get action number.
       *
       * @return Action number
       */
      public int getAction()
       {
        return this.action;
       }

     }


    /**
     * Enum for camera.
     */
    public enum Camera
     {
      /**
       * Left camera.
       */
      LEFT(0),

      /**
       * Right camera.
       */
      RIGHT(1),

      /**
       * Both cameras.
       */
      BOTH(2),

      /**
       * Live camera.
       */
      LIVE(3);


      /**
       * Action number.
       */
      private final int action;


      /**
       * Ordinal constructor.
       *
       * @param action Action number
       */
      Camera(final int action)
       {
        this.action = action;
       }


      /**
       * Get action number.
       *
       * @return Action number
       */
      public int getAction()
       {
        return this.action;
       }

     }


    /**
     * Enum for automatic image improvement.
     */
    public enum Automatic
     {
      /**
       * Automatic off.
       */
      OFF(0),

      /**
       * Automatic auto.
       */
      AUTO(1),

      /**
       * Automatic extended.
       */
      EXTENDED(2),

      /**
       * Automatic aggressive.
       */
      AGGRESSIVE(3);


      /**
       * Action number.
       */
      private final int action;


      /**
       * Ordinal constructor.
       *
       * @param action Action number
       */
      Automatic(final int action)
       {
        this.action = action;
       }


      /**
       * Get action number.
       *
       * @return Action number
       */
      public int getAction()
       {
        return this.action;
       }

     }


    /**
     * Enum for mirror image.
     *
     * no|vertical|horizontal|both
     */
    public enum Mirror
     {
      /**
       * No mirroring.
       */
      NO(0),

      /**
       * Mirror vertical.
       */
      VERTICAL(1),

      /**
       * Mirror horizontal.
       */
      HORIZONTAL(2),

      /**
       * Mirror vertical and horizontal.
       */
      BOTH(3);


      /**
       * Action number.
       */
      private final int action;


      /**
       * Ordinal constructor.
       *
       * @param action Action number
       */
      Mirror(final int action)
       {
        this.action = action;
       }


      /**
       * Get action number.
       *
       * @return Action number
       */
      public int getAction()
       {
        return this.action;
       }

     }


    /**
     * Enum for error handling.
     */
    public enum Error
     {
      /**
       * Picture no frame available.
       */
      PICTURE(0),

      /**
       * Empty image.
       */
      EMPTY(1),

      /**
       * Content type only.
       */
      CONTENT(2),

      /**
       * Current live image.
       */
      CURRENT(3);


      /**
       * Action number.
       */
      private final int action;


      /**
       * Ordinal constructor.
       *
       * @param action Action number
       */
      Error(final int action)
       {
        this.action = action;
       }


      /**
       * Get action number.
       *
       * @return Action number
       */
      public int getAction()
       {
        return this.action;
       }

     }


    /**
     * Enum for noise suppress.
     */
    public enum PipPosition
     {
      /**
       * Bottom right.
       */
      BOT_RIGHT(0),

      /**
       * Bottom left.
       */
      BOT_LEFT(1),

      /**
       * Top right.
       */
      TOP_RIGHT(2),

      /**
       * Top left.
       */
      TOP_LEFT(3);


      /**
       * Action number.
       */
      private final int action;


      /**
       * Ordinal constructor.
       *
       * @param action Action number
       */
      PipPosition(final int action)
       {
        this.action = action;
       }


      /**
       * Get action number.
       *
       * @return Action number
       */
      public int getAction()
       {
        return this.action;
       }

     }


    /**
     * Enum for display mode.
     */
    public enum DisplayMode
     {
      /**
       * Simple.
       */
      SIMPLE(0),

      /**
       * Lens correction l11.
       */
      LENSCORR_L11(1),

      /**
       * Lens correction l22.
       */
      LENSCORR_L22(2),

      /**
       * Surround.
       */
      SURROUND(3),

      /**
       * Panorama.
       */
      PANORAMA(4),

      /**
       * Panorama double.
       */
      PANO_DBL(5),

      /**
       * Panorama focus.
       */
      PANO_FOCUS(6),

      /**
       * Picture in picture.
       */
      PIP(7),

      /**
       * Piz.
       */
      PIZ(8);


      /**
       * Action number.
       */
      private final int action;


      /**
       * Ordinal constructor.
       *
       * @param action Action number
       */
      DisplayMode(final int action)
       {
        this.action = action;
       }


      /**
       * Get action number.
       *
       * @return Action number
       */
      public int getAction()
       {
        return this.action;
       }

     }


    /**
     * Default constructor.
     */
    public ImageParams()
     {
      super();
     }


    /**
     * Build.
     *
     * @return ImageParamBuilder
     */
    public ImageParams build()
     {
      return this;
     }


    /**
     * Add ampersand if required.
     *
     * @param params StringBuilder
     */
    private static void addAmpersand(final StringBuilder params)
     {
      if (params.length() > 0)
       {
        params.append('&');
       }
     }


    /**
     * Create url parameter string.
     *
     * @return Url parameter string
     * @throws UnsupportedEncodingException  Unsupported encoding exception
     */
    public String getUrlParams() throws UnsupportedEncodingException
     {
      final var params = new StringBuilder();
      if ((this.width != 0) && (this.height != 0))
       {
        // addAmpersand(params);
        params.append(SIZE);
        params.append(this.width);
        params.append('x');
        params.append(this.height);
       }
      if ((this.custWidth != 0) && (this.custHeight != 0))
       {
        addAmpersand(params);
        params.append("customsize="); //$NON-NLS-1$
        params.append(this.custWidth);
        params.append('x');
        params.append(this.custHeight);
       }
      if (this.brightness != null)
       {
        addAmpersand(params);
        params.append("brightness="); //$NON-NLS-1$
        params.append(this.brightness);
       }
      if (this.backlight != null)
       {
        addAmpersand(params);
        params.append("backlight="); //$NON-NLS-1$
        params.append(this.backlight);
       }
      if (this.color != null)
       {
        addAmpersand(params);
        params.append("color="); //$NON-NLS-1$
        params.append(this.color);
       }
      if (this.blue != null)
       {
        addAmpersand(params);
        params.append("blue="); //$NON-NLS-1$
        params.append(this.blue);
       }
      if (this.red != null)
       {
        addAmpersand(params);
        params.append("red="); //$NON-NLS-1$
        params.append(this.red);
       }
      if (this.sharpen != null)
       {
        addAmpersand(params);
        params.append("sharpen="); //$NON-NLS-1$
        params.append(this.sharpen);
       }
      if (this.quality != 0)
       {
        addAmpersand(params);
        params.append("quality="); //$NON-NLS-1$
        params.append(this.quality);
       }
      if (this.rotate != null)
       {
        addAmpersand(params);
        params.append("rotate="); //$NON-NLS-1$
        params.append(this.rotate);
       }
      if (this.textBgOpacity != null)
       {
        addAmpersand(params);
        params.append("textbgopacity="); //$NON-NLS-1$
        params.append(this.textBgOpacity);
       }
      if (this.displayFontSize != 0)
       {
        addAmpersand(params);
        params.append("displayfontsize="); //$NON-NLS-1$
        params.append(this.displayFontSize);
       }
      if (this.date != null)
       {
        addAmpersand(params);
        params.append("date="); //$NON-NLS-1$
        params.append(this.date);
       }
      if (this.textcolor != null)
       {
        addAmpersand(params);
        params.append("textcolor="); //$NON-NLS-1$
        params.append(String.format("0x%06X", this.textcolor)); //$NON-NLS-1$
       }
      if (this.textbgcolor != null)
       {
        addAmpersand(params);
        params.append("textbgcolor="); //$NON-NLS-1$
        params.append(String.format("0x%06X", this.textbgcolor)); //$NON-NLS-1$
       }
      if (this.stitchmode != null)
       {
        addAmpersand(params);
        params.append("stitchmode="); //$NON-NLS-1$
        params.append(this.stitchmode.booleanValue() ? "on" : "off"); //$NON-NLS-1$ //$NON-NLS-2$
       }
      if (this.doublePip != null)
       {
        addAmpersand(params);
        params.append("double_pip="); //$NON-NLS-1$
        params.append(this.doublePip.booleanValue() ? "on" : "off"); //$NON-NLS-1$ //$NON-NLS-2$
       }
      if (this.showlogo != null)
       {
        addAmpersand(params);
        params.append("showlogo="); //$NON-NLS-1$
        params.append(URLEncoder.encode(this.showlogo, StandardCharsets.UTF_8.toString()));
       }
      if (this.print != null)
       {
        addAmpersand(params);
        params.append("print="); //$NON-NLS-1$
        params.append(URLEncoder.encode(this.print, StandardCharsets.UTF_8.toString()));
       }
      if (this.noisesuppress != null)
       {
        addAmpersand(params);
        params.append("noisesuppress="); //$NON-NLS-1$
        params.append(this.noisesuppress.name().toLowerCase(Locale.getDefault()));
       }
      if (this.textdisplay != null)
       {
        addAmpersand(params);
        params.append("textdisplay="); //$NON-NLS-1$
        params.append(this.textdisplay.name().toLowerCase(Locale.getDefault()));
       }
      if (this.camera != null)
       {
        addAmpersand(params);
        params.append("camera="); //$NON-NLS-1$
        params.append(this.camera.name().toLowerCase(Locale.getDefault()));
       }
      if (this.automatic != null)
       {
        addAmpersand(params);
        params.append("automatic="); //$NON-NLS-1$
        params.append(this.automatic.name().toLowerCase(Locale.getDefault()));
       }
      if (this.mirror != null)
       {
        addAmpersand(params);
        params.append("mirror="); //$NON-NLS-1$
        params.append(this.mirror.name().toLowerCase(Locale.getDefault()));
       }
      if (this.error != null)
       {
        addAmpersand(params);
        params.append("error="); //$NON-NLS-1$
        params.append(this.error.name().toLowerCase(Locale.getDefault()));
       }
      if (this.pipposition != null)
       {
        addAmpersand(params);
        params.append("pipposition="); //$NON-NLS-1$
        params.append(this.pipposition.name().toLowerCase(Locale.getDefault()));
       }
      if (this.displayMode != null)
       {
        addAmpersand(params);
        params.append("displaymode="); //$NON-NLS-1$
        params.append(this.displayMode.name().toLowerCase(Locale.getDefault()));
       }
      return params.toString();
     }


    /**
     * Set image size (must be 4:3).
     *
     * @param width Image width
     * @param height Image height
     * @return ImageParams
     * @throws IllegalArgumentException When parameter is out of range
     */
    public ImageParams size(final int width, final int height)
     {
      if ((width < 0) || (width > 2048))
       {
        throw new IllegalArgumentException(MHttpApi.WRONG_WIDTH);
       }
      if ((height < 0) || (height > 1536))
       {
        throw new IllegalArgumentException(MHttpApi.WRONG_HEIGHT);
       }
      // TODO if 0 calculate from other value
      // TODO check width / 4 == height / 3
      this.width = width;
      this.height = height;
      return this;
     }


    /**
     * Set image custom size (must be 4:3).
     *
     * @param width Image width
     * @param height Image height
     * @return ImageParams
     * @throws IllegalArgumentException When parameter is out of range
     */
    public ImageParams customSize(final int width, final int height)
     {
      if ((width < 0) || (width > 2048))
       {
        throw new IllegalArgumentException(MHttpApi.WRONG_WIDTH);
       }
      if ((height < 0) || (height > 1536))
       {
        throw new IllegalArgumentException(MHttpApi.WRONG_HEIGHT);
       }
      // TODO if 0 calculate from other value
      // TODO check width / 4 == height / 3
      this.custWidth = width;
      this.custHeight = height;
      return this;
     }


    /**
     * Set image brightness.
     *
     * @param brightness Brightness (-10..10)
     * @return ImageParams
     * @throws IllegalArgumentException When parameter is out of range
     */
    public ImageParams brightness(final int brightness)
     {
      if ((brightness < -10) || (brightness > 10))
       {
        throw new IllegalArgumentException("brigthness must be -10..10"); //$NON-NLS-1$
       }
      this.brightness = Integer.valueOf(brightness);
      return this;
     }


    /**
     * Set image backlight.
     *
     * @param backlight Backlight (-10..10)
     * @return ImageParams
     * @throws IllegalArgumentException When parameter is out of range
     */
    public ImageParams backlight(final int backlight)
     {
      if ((backlight < -10) || (backlight > 10))
       {
        throw new IllegalArgumentException("backlight must be -10..10"); //$NON-NLS-1$
       }
      this.backlight = Integer.valueOf(backlight);
      return this;
     }


    /**
     * Set image color saturation.
     *
     * @param color Color saturation (-10..10)
     * @return ImageParams
     * @throws IllegalArgumentException When parameter is out of range
     */
    public ImageParams color(final int color)
     {
      if ((color < -10) || (color > 10))
       {
        throw new IllegalArgumentException("color must be -10..10"); //$NON-NLS-1$
       }
      this.color = Integer.valueOf(color);
      return this;
     }


    /**
     * Set image blue adjustment.
     *
     * @param blue Blue adjustment (-10..10)
     * @return ImageParams
     * @throws IllegalArgumentException When parameter is out of range
     */
    public ImageParams blue(final int blue)
     {
      if ((blue < -10) || (blue > 10))
       {
        throw new IllegalArgumentException("blue must be -10..10"); //$NON-NLS-1$
       }
      this.blue = Integer.valueOf(blue);
      return this;
     }


    /**
     * Set image red adjustment.
     *
     * @param red Red adjustment (-10..10)
     * @return ImageParams
     * @throws IllegalArgumentException When parameter is out of range
     */
    public ImageParams red(final int red)
     {
      if ((red < -10) || (red > 10))
       {
        throw new IllegalArgumentException("red must be -10..10"); //$NON-NLS-1$
       }
      this.red = Integer.valueOf(red);
      return this;
     }


    /**
     * Set image sharpen.
     *
     * @param sharpen Sharpen (0..10)
     * @return ImageParams
     * @throws IllegalArgumentException When parameter is out of range
     */
    public ImageParams sharpen(final int sharpen)
     {
      if ((sharpen < 0) || (sharpen > 10))
       {
        throw new IllegalArgumentException("sharpen must be 0..10"); //$NON-NLS-1$
       }
      this.sharpen = Integer.valueOf(sharpen);
      return this;
     }


    /**
     * Set image jpeg quality.
     *
     * @param quality Quality (10..90)
     * @return ImageParams
     * @throws IllegalArgumentException When parameter is out of range
     */
    public ImageParams quality(final int quality)
     {
      if ((quality < 10) || (quality > 90))
       {
        throw new IllegalArgumentException("quality must be 10..90"); //$NON-NLS-1$
       }
      this.quality = quality;
      return this;
     }


    /**
     * Set image rotation.
     *
     * @param rotate Rotate (0..360)
     * @return ImageParams
     * @throws IllegalArgumentException When parameter is out of range
     */
    public ImageParams rotate(final int rotate)
     {
      if ((rotate < 0) || (rotate > 360))
       {
        throw new IllegalArgumentException("sharpen must be 0..360"); //$NON-NLS-1$
       }
      this.rotate = Integer.valueOf(rotate);
      return this;
     }


    /**
     * Set text background opacity.
     *
     * @param textbgopacity Text background opacity (0..100)
     * @return ImageParams
     * @throws IllegalArgumentException When parameter is out of range
     */
    public ImageParams textbgopacity(final int textbgopacity)
     {
      if ((textbgopacity < 0) || (textbgopacity > 100))
       {
        throw new IllegalArgumentException("textbgopacity must be 0..100"); //$NON-NLS-1$
       }
      this.textBgOpacity = Integer.valueOf(textbgopacity);
      return this;
     }


    /**
     * Set display font size.
     *
     * @param displayfontsize Text background opacity (8..24)
     * @return ImageParams
     * @throws IllegalArgumentException When parameter is out of range
     */
    public ImageParams displayfontsize(final int displayfontsize)
     {
      if ((displayfontsize < 8) || (displayfontsize > 24))
       {
        throw new IllegalArgumentException("textbgopacity must be 8..24"); //$NON-NLS-1$
       }
      this.displayFontSize = displayfontsize;
      return this;
     }


    /**
     * Set date type.
     *
     * @param date Date type (0..7)
     * @return ImageParams
     * @throws IllegalArgumentException When parameter is out of range
     */
    public ImageParams date(final int date)
     {
      if ((date < 0) || (date > 7))
       {
        throw new IllegalArgumentException("date type must be 0..7"); //$NON-NLS-1$
       }
      this.date = Integer.valueOf(date);
      return this;
     }


    /**
     * Set text color.
     *
     * @param textcolor Text color (0x000000-0xFFFFFF)
     * @return ImageParams
     * @throws IllegalArgumentException When parameter is out of range
     */
    public ImageParams textcolor(final int textcolor)
     {
      if ((textcolor < 0x000000) || (textcolor > 0xFFFFFF))
       {
        throw new IllegalArgumentException("textcolor must be 0x000000-0xFFFFFF"); //$NON-NLS-1$
       }
      this.textcolor = Integer.valueOf(textcolor);
      return this;
     }


    /**
     * Set text background color.
     *
     * @param textbgcolor Text background color (0x000000-0xFFFFFF)
     * @return ImageParams
     * @throws IllegalArgumentException When parameter is out of range
     */
    public ImageParams textbgcolor(final int textbgcolor)
     {
      if ((textbgcolor < 0x000000) || (textbgcolor > 0xFFFFFF))
       {
        throw new IllegalArgumentException("textbgcolor must be 0x000000-0xFFFFFF"); //$NON-NLS-1$
       }
      this.textbgcolor = Integer.valueOf(textbgcolor);
      return this;
     }


    /**
     * Set stitch mode.
     *
     * @param stitchmode Stitch mode true: on; false: off;
     * @return ImageParams
      */
    public ImageParams stitchmode(final boolean stitchmode)
     {
      this.stitchmode = Boolean.valueOf(stitchmode);
      return this;
     }


    /**
     * Set double picture in picture.
     *
     * @param doublePip Double picture in picture mode true: on; false: off;
     * @return ImageParams
      */
    public ImageParams doublePip(final boolean doublePip)
     {
      this.doublePip = Boolean.valueOf(doublePip);
      return this;
     }


    /**
     * Set logo name to show.
     *
     * @param showlogo Logo name
     * @return ImageParams
     * @throws IllegalArgumentException If showlogo not matches ^[A-Za-z0-9_-]+$
      */
    public ImageParams showlogo(final String showlogo)
     {
      if (!MHttpApi.LOGONAME_REGEXP.matcher(showlogo).matches())
       {
        throw new IllegalArgumentException("Illegal logo name, must be [A-Za-z0-9_-]+"); //$NON-NLS-1$
       }
      this.showlogo = showlogo;
      return this;
     }


    /**
     * Set print content.
     *
     * @param print Print content
     * @return ImageParams
     * @throws IllegalArgumentException  If print does not match ^[A-Za-z0-9_-]+$
     *
     * TODO line break
      */
    public ImageParams print(final String print)
     {
      if (!MHttpApi.PRINTCONTENT_REGEXP.matcher(print).matches())
       {
        throw new IllegalArgumentException("Illegal print content, must be [A-Za-z0-9_-]+"); //$NON-NLS-1$
       }
      this.print = print;
      return this;
     }


    /**
     * Set noise suppress.
     *
     * @param noisesuppress Noise suppress (off, low, high)
     * @return ImageParams
     * @throws IllegalArgumentException When parameter is out of range
     */
    public ImageParams noisesuppress(final NoiseSuppress noisesuppress)
     {
      this.noisesuppress = noisesuppress;
      return this;
     }


    /**
     * Set text display.
     *
     * @param textdisplay Text display (disable, enable, datetime)
     * @return ImageParams
     * @throws IllegalArgumentException When parameter is out of range
     */
    public ImageParams textdisplay(final TextDisplay textdisplay)
     {
      this.textdisplay = textdisplay;
      return this;
     }


    /**
     * Set camera.
     *
     * @param camera Camera (left, right, both, live)
     * @return ImageParams
     */
    public ImageParams camera(final Camera camera)
     {
      this.camera = camera;
      return this;
     }


    /**
     * Set automatic.
     *
     * @param automatic Automatic (off, auto, extended, aggressive)
     * @return ImageParams
     */
    public ImageParams automatic(final Automatic automatic)
     {
      this.automatic = automatic;
      return this;
     }


    /**
     * Set automatic.
     *
     * @param mirror Mirror image (off, auto, extended, aggressive)
     * @return ImageParams
     */
    public ImageParams mirror(final Mirror mirror)
     {
      this.mirror = mirror;
      return this;
     }


    /**
     * Set error hanling.
     *
     * @param error Error image (picture, empty, content, current)
     * @return ImageParams
     */
    public ImageParams error(final Error error)
     {
      this.error = error;
      return this;
     }


    /**
     * Set picture in picture position.
     *
     * @param pipposition Picture in picture position (bot_right, bot_left, top_right, top_left)
     * @return ImageParams
     */
    public ImageParams pipposition(final PipPosition pipposition)
     {
      this.pipposition = pipposition;
      return this;
     }


    /**
     * Set picture in picture position.
     *
     * @param displaymode Display mode (simple, lenscorr_l11, lenscorr_l22, surround, panorama, pano_dbl, pano_focus, pip, piz)
     * @return ImageParams
     */
    public ImageParams displaymode(final DisplayMode displaymode)
     {
      this.displayMode = displaymode;
      return this;
     }

   }


  /**
   * Fast stream parameters.
   */
  @SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
  public static class FaststreamParams
   {
    /**
     * Image width.
     */
    private int width;

    /**
     * Image height.
     */
    private int height;

    /**
     * iframe refresh.
     */
    private Integer iframerefresh;

    /**
     * jp header update.
     */
    private Integer jpheaderupdate;

    /**
     * jp header refresh.
     */
    private Integer jpheaderrefresh;

    /**
     * Quality.
     */
    private int quality;

    /**
     * Frames per second.
     */
    private Integer fps;

    /**
     * Frame count.
     */
    private Integer framecount;

    /**
     * Need content length.
     */
    private Boolean needlength;

    /**
     * HTML output format.
     */
    private Boolean html;

    /**
     * No audio.
     */
    private Boolean noaudio;

    /**
     * Preview output.
     */
    private Boolean preview;

    /**
     * Error handling.
     */
    private Error error;

    /**
     * Camera.
     */
    private Camera camera;

    /**
     * Stream.
     */
    private Stream stream;


    /**
     * Enum for error handling.
     */
    public enum Error
     {
      /**
       * Picture no frame available.
       */
      PICTURE(0),

      /**
       * Empty image.
       */
      EMPTY(1),

      /**
       * Content type only.
       */
      CONTENT(2),

      /**
       * Current live image.
       */
      CURRENT(3);


      /**
       * Action number.
       */
      private final int action;


      /**
       * Ordinal constructor.
       *
       * @param action Action number
       */
      Error(final int action)
       {
        this.action = action;
       }


      /**
       * Get action number.
       *
       * @return Action number
       */
      public int getAction()
       {
        return this.action;
       }

     }


    /**
     * Enum for camera.
     */
    public enum Camera
     {
      /**
       * Left camera.
       */
      LEFT(0),

      /**
       * Right camera.
       */
      RIGHT(1),

      /**
       * Both cameras.
       */
      BOTH(2);


      /**
       * Action number.
       */
      private final int action;


      /**
       * Ordinal constructor.
       *
       * @param action Action number
       */
      Camera(final int action)
       {
        this.action = action;
       }


      /**
       * Get action number.
       *
       * @return Action number
       */
      public int getAction()
       {
        return this.action;
       }

     }


    /**
     * Enum for stream type.
     */
    public enum Stream
     {
      /**
       * Full.
       */
      FULL(0),

      /**
       * MxPG.
       */
      MXPEG(1),

      /**
       * mxg.
       */
      MXG(2);


      /**
       * Action number.
       */
      private final int action;


      /**
       * Ordinal constructor.
       *
       * @param action Action number
       */
      Stream(final int action)
       {
        this.action = action;
       }


      /**
       * Get action number.
       *
       * @return Action number
       */
      public int getAction()
       {
        return this.action;
       }

     }


    /**
     * Default constructor.
     */
    public FaststreamParams()
     {
      super();
     }


    /**
     * Build.
     *
     * @return FaststreamParamBuilder
     */
    public FaststreamParams build()
     {
      return this;
     }


    /**
     * Add ampersand if required.
     *
     * @param params StringBuilder
     */
    private static void addAmpersand(final StringBuilder params)
     {
      if (params.length() > 0)
       {
        params.append('&');
       }
     }


    /**
     * Create url parameter string.
     *
     * @return Url parameter string
     * @throws UnsupportedEncodingException  Unsupported encoding exception
     */
    public String getUrlParams() throws UnsupportedEncodingException
     {
      final var params = new StringBuilder(28);
      if ((this.width != 0) && (this.height != 0))
       {
        // addAmpersand(params);
        params.append(SIZE);
        params.append(this.width);
        params.append('x');
        params.append(this.height);
       }
      if (this.iframerefresh != null)
       {
        addAmpersand(params);
        params.append("iframerefresh="); //$NON-NLS-1$
        params.append(this.iframerefresh);
       }
      if (this.jpheaderupdate != null)
       {
        addAmpersand(params);
        params.append("jpheaderupdate="); //$NON-NLS-1$
        params.append(this.jpheaderupdate);
       }
      if (this.jpheaderrefresh != null)
       {
        addAmpersand(params);
        params.append("jpheaderrefresh="); //$NON-NLS-1$
        params.append(this.jpheaderrefresh);
       }
      if (this.quality != 0)
       {
        addAmpersand(params);
        params.append("quality="); //$NON-NLS-1$
        params.append(this.quality);
       }
      if (this.fps != null)
       {
        addAmpersand(params);
        params.append("fps="); //$NON-NLS-1$
        params.append(this.fps);
       }
      if (this.framecount != null)
       {
        addAmpersand(params);
        params.append("framecount="); //$NON-NLS-1$
        params.append(this.framecount);
       }
      if ((this.needlength != null) && this.needlength.booleanValue())
       {
        addAmpersand(params);
        params.append("needlength"); //$NON-NLS-1$
       }
      if ((this.html != null) && this.html.booleanValue())
       {
        addAmpersand(params);
        params.append("html"); //$NON-NLS-1$
       }
      if ((this.noaudio != null) && this.noaudio.booleanValue())
       {
        addAmpersand(params);
        params.append("noaudio"); //$NON-NLS-1$
       }
      if ((this.preview != null) && this.preview.booleanValue())
       {
        addAmpersand(params);
        params.append("preview"); //$NON-NLS-1$
       }
      if (this.error != null)
       {
        addAmpersand(params);
        params.append("error="); //$NON-NLS-1$
        params.append(this.error.name().toLowerCase(Locale.getDefault()));
       }
      if (this.camera != null)
       {
        addAmpersand(params);
        params.append("camera="); //$NON-NLS-1$
        params.append(this.camera.name().toLowerCase(Locale.getDefault()));
       }
      if (this.stream != null)
       {
        addAmpersand(params);
        params.append("stream="); //$NON-NLS-1$
        params.append(this.stream.name().toLowerCase(Locale.getDefault()));
       }
      return params.toString();
     }


    /**
     * Set image size (must be 4:3).
     *
     * @param width Image width
     * @param height Image height
     * @return FaststreamParams
     * @throws IllegalArgumentException When parameter is out of range
     */
    public FaststreamParams size(final int width, final int height)
     {
      if ((width < 0) || (width > 2048))
       {
        throw new IllegalArgumentException(MHttpApi.WRONG_WIDTH);
       }
      if ((height < 0) || (height > 1536))
       {
        throw new IllegalArgumentException(MHttpApi.WRONG_HEIGHT);
       }
      // TODO if 0 calculate from other value
      // TODO check width / 4 == height / 3
      this.width = width;
      this.height = height;
      return this;
     }


    /**
     * iframe refresh.
     *
     * @param refresh Refresh rate 0..60
     * @return FaststreamParams
     * @throws IllegalArgumentException When parameter is out of range
     */
    public FaststreamParams iframerefresh(final int refresh)
     {
      if ((refresh < 0) || (refresh > 60))
       {
        throw new IllegalArgumentException("Wrong refresh (0-60)"); //$NON-NLS-1$
       }
      this.iframerefresh = refresh;
      return this;
     }


    /**
     * jp header update.
     *
     * @param update jp header update (0..1000)
     * @return FaststreamParams
     * @throws IllegalArgumentException When parameter is out of range
     */
    public FaststreamParams jpheaderupdate(final int update)
     {
      if ((update < 0) || (update > 1000))
       {
        throw new IllegalArgumentException("Wrong update (0-1000)"); //$NON-NLS-1$
       }
      this.jpheaderupdate = update;
      return this;
     }


    /**
     * jp header refresh.
     *
     * @param refresh jp header refresh (0..60)
     * @return FaststreamParams
     * @throws IllegalArgumentException When parameter is out of range
     */
    public FaststreamParams jpheaderrefresh(final int refresh)
     {
      if ((refresh < 0) || (refresh > 60))
       {
        throw new IllegalArgumentException("Wrong refresh (0-60)"); //$NON-NLS-1$
       }
      this.jpheaderrefresh = refresh;
      return this;
     }


    /**
     * jpeg quality.
     *
     * @param quality Jpeg quality (10..90)
     * @return FaststreamParams
     * @throws IllegalArgumentException When parameter is out of range
     */
    public FaststreamParams quality(final int quality)
     {
      if ((quality < 10) || (quality > 90))
       {
        throw new IllegalArgumentException("Wrong quality (10-90)"); //$NON-NLS-1$
       }
      this.quality = quality;
      return this;
     }


    /**
     * Frames per second.
     *
     * @param fps Frames per second (0..144)
     * @return FaststreamParams
     * @throws IllegalArgumentException When parameter is out of range
     */
    public FaststreamParams fps(final int fps)
     {
      if ((fps < 0) || (fps > 144))
       {
        throw new IllegalArgumentException("Wrong fps (0-144)"); //$NON-NLS-1$
       }
      this.fps = fps;
      return this;
     }


    /**
     * Frame count.
     *
     * @param framecount Frames per second (0..144)
     * @return FaststreamParams
     * @throws IllegalArgumentException When parameter is out of range
     */
    public FaststreamParams framecount(final int framecount)
     {
      if (framecount < 0)
       {
        throw new IllegalArgumentException("Wrong framecount (0-..)"); //$NON-NLS-1$
       }
      this.framecount = framecount;
      return this;
     }


    /**
     * Need content length.
     *
     * @param needlength Need length
     * @return FaststreamParams
     */
    public FaststreamParams needlength(final boolean needlength)
     {
      this.needlength = needlength;
      return this;
     }


    /**
     * HTML output.
     *
     * @param html Html output format
     * @return FaststreamParams
     */
    public FaststreamParams html(final boolean html)
     {
      this.html = html;
      return this;
     }


    /**
     * No audio output.
     *
     * @param noaudio No audio
     * @return FaststreamParams
     */
    public FaststreamParams noaudio(final boolean noaudio)
     {
      this.noaudio = noaudio;
      return this;
     }


    /**
     * Preview output.
     *
     * @param preview Preview outpu.
     * @return FaststreamParams
     */
    public FaststreamParams preview(final boolean preview)
     {
      this.preview = preview;
      return this;
     }


    /**
     * Set error hanling.
     *
     * @param error Error image (picture, empty, content, current)
     * @return FaststreamParams
     */
    public FaststreamParams error(final Error error)
     {
      this.error = error;
      return this;
     }


    /**
     * Set camera.
     *
     * @param camera Camera (left, right, both)
     * @return FaststreamParams
     */
    public FaststreamParams camera(final Camera camera)
     {
      this.camera = camera;
      return this;
     }


    /**
     * Set stream type.
     *
     * @param stream Stream type (full, MxPEG, mxg)
     * @return FaststreamParams
     */
    public FaststreamParams stream(final Stream stream)
     {
      this.stream = stream;
      return this;
     }

   }


  /**
   * Event parameters.
   */
  public static class EventParams
   {
    /**
     * Image width.
     */
    private int width;

    /**
     * Image height.
     */
    private int height;



    /**
     * Default constructor.
     */
    public EventParams()
     {
      super();
     }


    /**
     * Build.
     *
     * @return EventParamBuilder
     */
    public EventParams build()
     {
      return this;
     }


    /**
     * Add ampersand if required.
     *
     * @param params StringBuilder
     */
    private static void addAmpersand(final StringBuilder params)
     {
      if (params.length() > 0)
       {
        params.append('&');
       }
     }


    /**
     * Create url parameter string.
     *
     * @return Url parameter string
     * @throws UnsupportedEncodingException  Unsupported encoding exception
     */
    public String getUrlParams() throws UnsupportedEncodingException
     {
      final var params = new StringBuilder();
      if ((this.width != 0) && (this.height != 0))
       {
        // addAmpersand(params);
        params.append(SIZE);
        params.append(this.width);
        params.append('x');
        params.append(this.height);
       }
      return params.toString();
     }


    /**
     * Set image size (must be 4:3).
     *
     * @param width Image width
     * @param height Image height
     * @return FaststreamParams
     * @throws IllegalArgumentException When parameter is out of range
     */
    public EventParams size(final int width, final int height)
     {
      if ((width < 0) || (width > 2048))
       {
        throw new IllegalArgumentException(MHttpApi.WRONG_WIDTH);
       }
      if ((height < 0) || (height > 1536))
       {
        throw new IllegalArgumentException(MHttpApi.WRONG_HEIGHT);
       }
      // TODO if 0 calculate from other value
      // TODO check width / 4 == height / 3
      this.width = width;
      this.height = height;
      return this;
     }

    /*
    sequence=head|tail|0..999999
    offset=-999999..999999
    eno=--1..99999
    alarmimage=head|tail|0..999999 "." --1..99999
    length=1..10000
    timespan=1..
    filter=VM|UC|PE|TT|*|..
    story=-99999..99999
    direct=-99999..99999
    source=auto|intern|extern
    volume=0|1|S|..
    error=picture|empty|content|current
    output=jpeg|jpeg_download|m1img|evtinfo|evtinfo_d|mxg|m1imgtab|alarmlist|alarmupdate|certificate|sequencecheck|alarmlist_extended
    include_mailbox_changes
    verify
    preview
    quality=10..90
    camera=right|left|both
    searchbytime_start=Sekunden.Mikrosekunden    Unix epoche
    */

   }

 }
