package com.la.receta.config.base

import java.security.MessageDigest

import scala.math.BigInt
import org.mindrot.jbcrypt.BCrypt

import scala.util.Random

object Util {
  def md5HashString(s: String): String = {
    val md = MessageDigest.getInstance("MD5")
    val digest = md.digest(s.getBytes)
    val big = BigInt(1, digest)
    val hashedString = big.toString(16)
    hashedString
  }

  def hashString(password: String): String = BCrypt.hashpw(password, BCrypt.gensalt())

  private val random = new Random(System.currentTimeMillis)

  def string10 = new String(Array.fill(10)((random.nextInt(26) + 65).toByte))

  /*
  public static boolean matching(String orig, String compare){
    String md5 = null;
    try{
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(compare.getBytes());
        byte[] digest = md.digest();
        md5 = new BigInteger(1, digest()).toString(16);

        return md5.equals(orig);

    } catch (NoSuchAlgorithmException e) {
        return false;
    }

    return false;
}
   */
}
