# JTel
Java Telegram API Client.

<p>MTProto implementation 
<p>Creating auth_key and server_salt
<p>Tl language support to serialize and deserialize objects from known types to tl and vice versa.
<p>Aes ige and pq resolver implementation belongs to ex3ndr.

<h3>Usage</h3>
<code>
        MtpEngine engine = MtpEngine.getInstance();
        engine.createSession(new MtpFileStorage(),new HttpTransport());
        TlObject sentCode = engine.invokeApiCall(
                new TlMethod("auth.sendCode")
                .put("phone_number","989118836748")
                .put("sms_type",0)
                .put("api_id",  ConfStorage.getInstance().getItem("api-id"))
                .put("api_hash",ConfStorage.getInstance().getItem("api-hash"))
                .put("lang_code","en")
        );

        System.out.println(sentCode);
        System.out.println(sentCode.get("phone_code_hash"));
</code>