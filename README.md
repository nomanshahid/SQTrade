# SQTrade
SQTrade is a platform that uses various financial models to analyze and predict stock movements and filter best plays. Developed in Scala, the application uses Apache Spark for processing stock data.

The stock data is currently being retrieved using Yahoo! Finance API for paramaters such as beta, market cap, and P/E ratio to be applied to relevant valuation models.

At its core, the project uses the Capital Asset Pricing Model (CAPM) to value selected stocks. The model accounts for systematic risk, expected market return, and risk-free asset return. 

<p align="center"><img src="https://wikimedia.org/api/rest_v1/media/math/render/svg/2371da17ef371eb0764f9879c97e685cfa2dc256"></p>

Several other models are used to predict future price movement. The project is constantly being improved to perform more accurate analysis.
