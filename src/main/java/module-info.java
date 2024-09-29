/*
 * Copyright (C) 2020 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */


/**
 * M http api module.
 */
module de.powerstat.m.httpapi
 {
  exports de.powerstat.m.httpapi;

  requires transitive java.xml;

  requires org.apache.logging.log4j;
  requires transitive de.powerstat.validation;

  requires transitive org.apache.httpcomponents.httpclient;
  requires org.apache.httpcomponents.httpcore;
  // requires org.apache.commons.codec;
  requires java.desktop;

 }
