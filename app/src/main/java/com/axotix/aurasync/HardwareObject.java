package com.axotix.aurasync;

public class HardwareObject {
  private String hardware;
  private String hardwareMeaning;
  public HardwareObject(String hardware, String hardwareMeaning) {
    this.hardware = hardware;
    this.hardwareMeaning = hardwareMeaning;
  }
  public String getHardware() {
    return hardware;
  }
  public String getHardwareMeaning() {
    return hardwareMeaning;
  }
}