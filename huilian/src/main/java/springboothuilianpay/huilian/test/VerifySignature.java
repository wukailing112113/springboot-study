package springboothuilianpay.huilian.test;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import static springboothuilianpay.huilian.test.GenerateKeyPair.hexStrToBytes;

public class VerifySignature {


    public void run1() {
        try {
            String pubkeyvalue = "30819f300d06092a864886f70d010101050003818d003081890281810093dbe94b47911b429ae9cbb7181651492a29532a74caef2f15385334d168d75d236b4752ccf24d29ed7f3fa5d52dc18d106f8f1d7fcafac546f9de5bb96633dc3e7b92cf527221aa736b97669e8a8389587cfe05aeaaad15f3f2409fea47ec00c1b81f6036c4c71bdba66230f67114a1e29ee29c5bb6d3f9b5d3171ad0b11fc90203010001";//这是GenerateKeyPair输出的公钥编码
            X509EncodedKeySpec bobPubKeySpec = new X509EncodedKeySpec(hexStrToBytes(pubkeyvalue));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey pubKey = keyFactory.generatePublic(bobPubKeySpec);

            String info = "002";
            byte[] signed = hexStrToBytes("21936579ad12de8f9c82febaddd049cf6e8ef8e8dd45276e7b0f405fc231871a3cbe0378e0230048354403fa375a1cf2ba41185a3947010569ebe5e0fb0c806604362fcb4870ba7d0f0362c4f82e07418ba99b7608c1b6619efabd0cf3e87e768344a3d9513822aaf2c94c9d38650235a7d2bb8b8d921d0520ec56923ae130b2");//这是SignatureData输出的数字签名
            java.security.Signature signetcheck = java.security.Signature.getInstance("MD5withRSA");
            signetcheck.initVerify(pubKey);
            signetcheck.update(info.getBytes());
            if (signetcheck.verify(signed)) {
                System.out.println("info=" + info);
                System.out.println("签名正常");
            } else{
                System.out.println("非签名正常");
            }
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
// TODO Auto-generated method stub
        VerifySignature s = new VerifySignature();
        s.run1();




    }
}