/*
 * Copyright (C) 2020-2023 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */


/**
 * M http api module.
 */
open module de.powerstat.m.httpapi
 {
  exports de.powerstat.m.httpapi;

  requires transitive java.xml;

  requires org.apache.logging.log4j;
  requires transitive de.powerstat.validation;

  requires transitive org.apache.httpcomponents.httpclient;
  requires org.apache.httpcomponents.httpcore;
  // requires org.apache.commons.codec;
  requires java.desktop;

  requires com.github.spotbugs.annotations;
  requires org.junit.jupiter.api;
  requires org.junit.platform.launcher;
  requires org.junit.platform.suite.api;
  requires org.junit.jupiter.params;
  // requires io.cucumber.java;
  // requires io.cucumber.junit.platform.engine;
  // requires nl.jqno.equalsverifier;

 }
