package com.souche.canal.ng.common;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Encrypt {
    private static final int KEY_LENGTH = 128;
    private static final String KEY_GENERATOR_ALGORITHM = "AES";
    private static final String SECURE_RANDOM_ALGORITHM = "SHA1PRNG";
    private static final String CHARSET_CODING = "utf-8";
    private static String ENCODE_RULES;

    static {
        ENCODE_RULES = getRules();
    }

    public static String getEncrypt(String original) {
        String encrypted = null;
        try {

            Cipher cipher = getCipher(Cipher.ENCRYPT_MODE);
            byte[] byteEncode = original.getBytes(CHARSET_CODING);
            byte[] bytes = cipher.doFinal(byteEncode);
            encrypted = new String(Base64.encodeBase64(bytes));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return encrypted;
    }

    public static String getDecrypt(String encrypted) {
        String decrypted = null;
        try {
            Cipher cipher = getCipher(Cipher.DECRYPT_MODE);
            byte[] byteContent = Base64.decodeBase64(encrypted);
            byte[] byteDecode = cipher.doFinal(byteContent);
            decrypted = new String(byteDecode, CHARSET_CODING);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return decrypted;
    }

    private static Cipher getCipher(int cipherModel) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_GENERATOR_ALGORITHM);
        SecureRandom random = SecureRandom.getInstance(SECURE_RANDOM_ALGORITHM);
        random.setSeed(ENCODE_RULES.getBytes());
        keyGenerator.init(KEY_LENGTH, random);
        SecretKey originalKey = keyGenerator.generateKey();
        byte[] raw = originalKey.getEncoded();
        SecretKey key = new SecretKeySpec(raw, KEY_GENERATOR_ALGORITHM);
        Cipher cipher = Cipher.getInstance(KEY_GENERATOR_ALGORITHM);
        cipher.init(cipherModel, key);
        return cipher;

    }

    private static String getRules() {
        return "L+mz6zDf\\0hO=|.'sdgu,cV5nd)_/D%zMH66?'T<i,.H%W,7eJFyO,6>6KIzil!UkhtiJG^GNAa8dt((-8Hc3r@(cGyeAD_xm%g4SO+grX@fGuzK#AoP)zbMrV\\\\O=JDWk3;azB\\?syVU!5qE(yl:HteH:'rgN$\\,nhziRix6f+(Ot5g,:e^wQ;z-4YNM8<N!#>XO+CyR5*C:M5n#>O7whIYG,0;0V:R.0X)w-#*7dQt0?140mp\\SGby;N\\GD(mo,Ban(!TiI?xpY&_F:400?&F?awn2=D4oP(?@dnj$_W,vm38OrZSo./Xr3l%4TqLe|/J<q*\\S|/qZ1!hHgGf?EBA.nh,Wpi%3dZ.'Tmi$l4:kG!MccNCC<.s#Do)8;fy$*?^U$4EkbEwPg9Av-N+Zzx6eQj5bmGL3;ps46qkfRZdTO%kb(k^6\\qk|6UB-VdYU#G(-ggT858#NYslN2dui:d;M,@e9B8TZWZcu)B3+I=:u@^l@lL-=v'KyF!71gywl&BV-g&nnD@RY-\\&HBhT^RH1:,5YdhZob6*-gN=ns1OI$X*M4!/(B&j/1x>-'Rs5'<^X2;$ws?)@jRi!OY$dc@%|Y=!;gzuhpv0f\\ziD(FqMkyK1B4jmGB+aSGv'!MfhBXS%>'UV;AyU0ogGpQM9!Ql-Da-4t3O-KwouS8;.Q>C_2ZTkmk+ipk-<)v:@2oJa@7v7JN4>r|jI2jgOAZUFu-7l6W1lgmP$orJFsfmba>L&k:2d*r6SKj8Yev#A%fy(vaAbesSTVfX\\^J.Efvl*-=L)6bHJpef;Z8oQj(R|IGQ4Qq>WVVGy$t?lT4X=:$b,<Cg4C<:Dj_swcyI>GEZX^oE%0)|B=LX:!uBz5Pp/Mpz*hDG_WJ>7j(PO8!4u/G2#1_\\mGI?fi=VI_n13,KWK7IR!8O3Qbms4\\0F*\\KAHH(.KoLq*nHa_b=B;$9;/f:G/7lZKm/P</hRZ>gu0fm<vOHKG!hX78;f|!*S4.u8:D390.U<+|rjkPX%jtWFd3xVTvmObnCUi(5Lhjb^Z)w%q)1x=iCo5Z:dcY,(cg/1^RzU'/-r9ngEK/Az0'&.z.ET)8LL\\0OTsbwq:2:SZb|Ig@hgTP@0B/>.R:3-=,D\\+iTey4%-vaJoQ>=Z(A$ygc+'-.8E=5_(=g?2A/ASPUe8>A56KQv9wAdcoHH1|!a-5!Az;8w';L%-(I-qvXSG*0naKMlC\\V2IO2HGS4-4W1?G(6wJ7E:dnxF_hvKy,ajRRl^QXMikS:y3wluENaLq?V*>|uJiWt75/!@Q14sjn2CX?1Dsg=gzJ^&5g,SD&ktP:+i':kia/G.*m/i1Z)cicxViR/(M+%R=Fo@b1XrgIqTS..1Sm=v9)EcW9LyCeM+qCMcosVQXb,1YcDB;2!4JV_F;o'H1Qu@MgCIxB46|z>,s^5,/aBK5;FUMpCqHR-.@P#Z$'kU0m!3divSGD,$yTklB|i8pWVStKylBx-;,4evHM%@.-1qqVH'9-pO)S,-?w30YspLC<Kv-H\\yujRD_WTVJN|ujAg(j_,M;HZ)QzFpn4_cyEheOUjY2-dh1T*d3\\Z3Y_#T:f#Bz@^7M?byBQ8&v9*:|Z_sOzS'!=k$VYw8h?8hkiR+WV\\)MTM!$-ZE+.)gYCF)K#nF|,mRA|moec#k0GEbctp3E;.=LVcVqv=*>xD15aq$CkA:fwo-;(|ZQB</=?Hcg;RD7Nd,1zB8t5>7hA;M3iu4P\\%q-<rmzU=$p$&sZ?O(rZ%Q(y(@G0\\ea5J.L5dMk0|5;udk?ga*!G$Jx90N+ulMw9JKSNBTn@mSK5S<\\l5O_V0hT&=p&xI5eu#BIA&;N;TTO0V(Y\\yF3=FUSsf|6(B.IN0uJb'*?<|RW^RLnOLc:I|/\\VvcWu-2m5*Utc4bBHUEVL&|CQA4S34I6I6P\\c#6gp6n\\m3'cmrt_RdFfH5!e-u>/^v=1v*s8W0KnWOuafiO(Oy.tlH\\4vfcLCmR|t+)$-<;XID:vxh@\\8OpQ'sTApgB4>1Y=k7jL.dDl(8lHJv1AT'3ACMA8.j6h?texxOheZcq(wiF@b,Z9QuS+Pu.7PC,(:8>M>!tU1y,yFrK8u.Z%ilws%.HBBE_ZP_>/e&gQ0%qgG|0GVe+Ss3IN50U:'<|U)kH44c?x,iWpj^U!Ih/B$/KCr8Hv=zalr5Y1lap\\D<aePT;Uu(s-S>K<Ph@Hlg7eE!pJuy_cx#C^OMSzRC72f^-Dbqf2AI9*)u*:h&ZtZ.ws#-JtX=zU>dWq6|r=X!f^2<P2)m^^lJ|Q_D'H\\df+$D22=_w>Jw-e8|DJms)j(Hj$7U9sh?Xy?deCc<j^aZZ.YrBWg_VqHxOdHq>z@H(LZ|P+9MYx1WB%F8k*jV#;o\\;Wv(0fm3Ya$7GyVV$zxpsS_=!E@mi3TlWHL2vD2fSF;CTTQyyO)qso*Hx&YB*TPc'g:Wa$.d/joUsU/MNw%aPKtoG(FOzvExJ2_rMtaI>RKwz=)oX-thdNcmKGjh>cgo7^4##WTjx69P|%:uLhI)pR%!(GM$,Ew\\jK(NvYS94yLi5qAkM:JBqJ2&JG=(7kJN<uT;A5jrsCLcmI*:<mnJ+?es6lgGC;OqI--(0f2b1o.,50m&6+,'K>!jveoqfz0qjr,9F4:gB&gf>k(feYj3e.'J&f_x>G6D@|A0\\V0-#CVTRdp#JP34XCSWMf5\\;Y.2cxl:gFxrM\\&R&W,Bw;shTOE-7B8Q1|pnWRw:'u*_:=*bL@i&:wyu\\WjWm5Z!/Yzr3o&Y#/srZ-Krsl-?h\\/Kk\\YfEF|cU@y-7f5Jm5@60jWgRLWiPbHHA(y0+X*pJP1O;y-d|F;AHkoV6#pR5Wh'Jk,6&ev'10*0Vo>+N=pz-GU6cwCTi?fnG@r1A*u-;?W8C;=#ew1&CI/6>><5cP+JlN5Kk^I%f;X#1idiFrHoWpEb<waRRi;JKtfGHo=4|jA=.r;Tl6nr40qV&!&cVn^!gHJ+,CsU7QW1$UX<kUOk4pCuV\\h9dy9VAh<&+BuQA<Dh\\bI0xXDD;gKF2F,Bz-wnvNb.K1J!Xe&n2^&&6gBzMlcFC(b3cGq7z9O.Y59|!EX4cv+b>;EB?|Bd7#9E$'YSvP4#R<:vDZ/e-Em26g4/:jxel?ud;Jfr:4:3ADQwn!2fcTDM8.1OB5gQVAgcj_YKA.Y*KY'H\\)5smtR.Bnr)>>51orQJjDLTyEpzA7,FnP34yOrk%NlANo'w&_e7u#=RlN(sb#%&L.jcSA'Xkfp2sOy5fD^JJP^7@x1CVJ:gsI^2WMQf>Fvv#T#dDRw9UD&bP;zw!L7F1byLPY222RW,nV2(gp496XTFaok&W#B7Oq6CR%H1|>VNPw80Yyy/_Hs1CEG?DI67apxgAXCFMCFi@_PAQ0Tw*\\I<_GZ28.%/1VI:k#E!K\\LhX&U*dCk4XUJQuUc+B>:xBQ^J^OeNJn;),KWp:9GfKJ+,gw!6r1mYTD<&_F5>%y7==5proX(G|/5Y^t?\\vd!&cs-y8P^D;=IH2om9Bcf8Q+<vU+s=ht&+/M(_@Q+GPo!qEuv*RDbMq-^$@6^(xQ0fQmoDEAW%CJMoUlpTKTJF(0h4cUtRv9Z(+1X8R#&BPmrVTQ.E\\!2$55.(-AV&|(7W@'g|A@SvX-R,J4Y1b(Qd3V+'7e_'XHj3o)yq!-2^adw50f|#BMC5<Mk+rJY>61W3gwo-$'YG7tHI!are4vI&|)1ifp?6hz>1\\VJ8ENRx*f=2J=L0xh6rAmpbc@UMu@Zx#agC;5KY@qx8<%,4XW3K<o7w<Bzs\\^?T+QkhmyaA1e|/%X@l51;ip&iIraANTYA>&ur&VEGel3i^gNnS?5;>|IUGEH(D7IKB_uMxM7:8Me-s;B#%2:w:5XFr5o|QJYm9P<c>C7|$K@6(7/5%ve6U?BD\\:PTy:<(cbG^o|.@|+<R4$7OeF4ZMp4zLcevVy,+)pNTv3_p-Gi_(KDN'57iI=B3yT'01|;U%P1gC;aGkn,Kvs9$?ztaoq1:>t/6\\/sQ5$,lJ\\M6;6+WDA8IHL+*tB'BO8+^DUi(MTEry1$S:6ae$xdy@X\\6Jk<G1Yd4)Cm@0LfJQ9q|E#y*B;-dt;Jp_oNfM=*2r_lgf*peY$m768r8Y!5?$BFYtfMhd5ve>CIr7.tWED1I<nHrQ.0@7NrA$dh'f?V+lrmBKgVe|a&\\+Fs\\t7W?_A#<kDIn1!I\\IJ/wwlVW/uhA2hYVn\\4<8%iJbQO_MH+)5>*0zyih:5jK_hhnU-q-3;,Hvh|3J:BT(1THB7/n!V5fd^DW.xWcfm3T!1l/Q:h)&8)WU)#X_psg(5v\\KrVaVi5kFgWG8tN_%wc\\x)w=-DxOWIfZ4A.$z?>cvXJ$98Ef^CpLiy84#vr:mF3:Y%2#!nqLF,yYUPC95/k;onGtKToTdbsDc*njYpS2Hk;e\\QhV|j)@!wCwp,m?EuCM7Ku0#Sz=r5QA*3Yb8z%!qo2&6@EyEzmL|DN?%,m.XwlG1(l-T1aQQ|u,uVR9gmq0,sDY+fs:Kz@*Ls82luOWE7;(_fYjzb?3K)V|)3\\ZYhz|.f!;yBD^j4=+eY?<s0AQ;Hj@D_Plvg3/rK!bK&?IH.|K&gcDSzrxsAb>_wY0&LjKpN0@QtXUZ:5ELXHj$F#8WBbqIH?Jq+!h#CjO-*:$MUwWdWTMLxox@c_q1z>p>P19L#mFt>xIoiUe&+IFBKO:A|V3?G3.W.&lSQ$suofF7FRU4Sf9TuP*51\\^&3bGx1Hh?NA,L.iMQ*h\\5cDu?DLsC6CG?DlX\\W5spK1Tx(R<:1o0Hwg<q::AA6x.<0b(Gn:ZctV(o05CN35nK#!0ycNpzqHV*kw9bcCj2zED4l;s3Yk,R*T.=m-'%-qmJ7X.>ZKM#1/+)4'ArHaCqt*KoNS7OQz@m2_nB(d(wK_=)aqd<>C7ZjY@%h7J_hnh9SXzmkOb@#+Jbif#/ug(Ux5fQP(lu(oC4v;1cGfchDgf;kG*/X9NIy)l2OU:(E(8xkekVhm9;%oKOc&S)o|Ka";
    }
}
