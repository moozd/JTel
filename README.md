#  JTel (Java Telegram API Client)

> MTProto implementation 
> Tl language support to serialize and deserialize objects from known types to tl and vice versa.
> Aes ige and pq resolver implementation belongs to @ex3ndr.

# Usage
```java
 try {
        TelegramApi api = TelegramApi.getInstance();
        String phone_number= "xxxxxxxxx"
        String phone_code_hash;  
        String phone_code = ""; // code from telegram (sms) 
        int sms_type = 0;
        String lang_code = 'en';
        TlObject sentCode  = api.sendCode(phone_number, sms_type, lang_code);
        phone_code_hash = sentCode.get("phone_code_hash");
        TlObject authorization  = api.auth.signIn(phone_number,phone_code_hash,phone_code);
 }catch (Exception e){
        //exception
 }

```
