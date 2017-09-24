# SQTrade
SQTrade is a platform that uses various financial models to analyze and predict stock movements and filter best plays. Developed in Scala, the application uses Apache Spark for machine learning and processing stock data.

The stock data is currently being retrieved using Yahoo! Finance API for paramaters such as beta, market cap, and P/E ratio to be applied to relevant valuation models.

At its core, the project uses the Capital Asset Pricing Model (CAPM) to value selected stocks. The model accounts for systematic risk, expected market return, and risk-free asset return. 

<p align="center"><img src="https://wikimedia.org/api/rest_v1/media/math/render/svg/2371da17ef371eb0764f9879c97e685cfa2dc256"></p>

Historical data of the selected stocks is processed in Spark to train the neural network to predict future values. The gradient descent algorithm is used to reduce the error rate in each iteration. The predicted and expected output is compared for integrity before considering future predictions of the stocks price.
