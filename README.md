sample test step:

Run DemoApplication.java as Java application
database: http://localhost:8080/h2-console/login.do?jsessionid=893cbdb073987cb91d3754dbed147db0

Login info:

Saved Settings:	
Generic H2 (Embedded)

Setting Name:	
Generic H2 (Embedded)

Driver Class:	org.h2.Driver
JDBC URL:	jdbc:h2:mem:testdb
username:sa
password (keep as empty):



Task 1 : 1Price aggregation 

output refer to the table 
TRADING_PAIR


Task 2: Create an api to retrieve the latest best aggregated price.
>curl -X GET http://localhost:8080/api/price
[{"priceId":null,"symbol":"ETHUSDT","bidPrice":2642.13,"bidQuantity":0.5,"bidSource":"HUOBI","askPrice":2641.87,"askQuantity":3.8784,"askSource":"BINANCE","priceDate":"2024-08-12T01:51:32.4311515"},{"priceId":null,"symbol":"BTCUSDT","bidPrice":60319.06,"bidQuantity":0.03768,"bidSource":"HUOBI","askPrice":60288.99,"askQuantity":0.28909,"askSource":"BINANCE","priceDate":"2024-08-12T01:51:32.4311515"},{"priceId":null,"symbol":"BTCETH","bidPrice":22.828672532879175,"bidQuantity":0.021902280970558893,"bidSource":"HUOBI","askPrice":22.820660443778245,"askQuantity":0.012667906816816805,"askSource":"BINANCE","priceDate":"2024-08-12T01:51:32.4311515"},{"priceId":null,"symbol":"ETHBTC","bidPrice":0.043819941252955144,"bidQuantity":1.92459,"bidSource":"BINANCE","askPrice":0.04380456194111779,"askQuantity":0.1513,"askSource":"HUOBI","priceDate":"2024-08-12T01:51:32.4311515"}]


Task 3. Create an api which allows users to trade based on the latest best aggregated
price.
>curl -X POST http://localhost:8080/api/trade ^
     -H "Content-Type: application/json" ^
     -d "{\"fromCurrency\": \"USDT\", \"toCurrency\": \"BTC\", \"fromAmountToTrade\": 5000, \"tradeType\": \"BUY\"}"


Trade executed successfully



Task 4. Create an api to retrieve the user’s crypto currencies wallet balance

use cmd:
>>    curl -X GET http://localhost:8080/api/wallet

output

[{"balance":45000.0,"cryptocurrency":"USDT","id":"userOne"},{"balance":0.0831482592,"cryptocurrency":"BTC","id":"userOne"}]


Task 5
Create an api to retrieve the user trading history
>>  curl -X GET http://localhost:8080/api/trading-history

{"trades":[{"REQUESTED_TRADE_DATE_TIME":"2024-08-12T01:47:06.416241","TRADE_REQUEST_ID":"638c4785-1a69-48f1-96df-6261e3b06bda","FROM_AMOUNT":5000.0,"USER_ID":"userOne","ACTUAL_TRADE_DATE_TIME":"2024-08-12T01:47:06.613693","TRADE_DETAILS":[{"tradeId":"64a5065f-b543-47e1-be5e-0fed56ad178e","actualTradeDateTime":"2024-08-12T01:47:06.611716","fromAmount":5000.0,"totalValue":0.08314825916647196}],"FROM_CURRENCY":"USDT","TRADE_TYPE":"BUY","TO_CURRENCY":"BTC"}]}






Task 4: 

Trade executed successfully

curl -X POST http://localhost:8080/api/trade ^
More?      -H "Content-Type: application/json" ^
More?      -d "{\"fromCurrency\": \"USDT\", \"toCurrency\": \"BTC\", \"fromAmountToTrade\": 5000, \"tradeType\": \"BUY\"}"