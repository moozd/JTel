# JTel
Java Telegram API Client.

<p>MTProto implementation 
<p>Creating auth_key and server_salt
<p>Tl language support to serialize and deserialize objects from known types to tl and vice versa.
<p>Aes ige and pq resolver implementation belongs to ex3ndr.

<h3>Usage</h3>

         String phone_number = "989118836748";
        int    sms_type     = 0;
        int    api_id       = ConfStorage.getInstance().getItem("api-id");
        String api_hash     = ConfStorage.getInstance().getItem("api-hash");
        String lang_code    = "en";


        TlObject sentCode = engine.invokeApiCall(
                new TlMethod("auth.sendCode")
                        .put("phone_number" ,phone_number)
                        .put("sms_type"     ,sms_type    )
                        .put("api_id"       ,api_id      )
                        .put("api_hash"     ,api_hash    )
                        .put("lang_code"    ,lang_code   )
        );

        if(sentCode.getPredicate().equals("rpc_error")) {
            return;
        }
        console.log("sms sent",sentCode);
        console.log("enter code");

        Scanner scanner = new Scanner(System.in);

        String phone_code_hash = sentCode.get("phone_code_hash");
        String phone_code      = scanner.next();

        TlObject auth = engine.invokeApiCall(
                new TlMethod("auth.signIn")
                        .put("phone_number"     ,phone_number)
                        .put("phone_code_hash"  ,phone_code_hash)
                        .put("phone_code",phone_code)
        );

        console.log("signIn done",auth);
        engine.saveSignIn(auth);

