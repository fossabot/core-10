package com.robertsanek.lifx;

import org.junit.Ignore;
import org.junit.Test;

public class LifxConnectorTest {

  @Test
  @Ignore("integration")
  public void name() {
    boolean b = new LifxConnector().triggerCoreDay();
    System.out.println("b = " + b);
  }
}