SELECT
 max(typeName) typeName,
 max(symbolName) symbolName,
 max(symbolCataId) symbolCataId,
 max(symbolCurrency) symbolCurrency,
 max(symbolSource) symbolSource,
 sum(amount) amount,
 max(tradeAccountId) tradeAccountId,
 max(execTime) execTime,
 max(rate) rate,
 case when sum(amount)>0 then min(amountsrc) else max(amountsrc) end AS amountsrc,
 case when sum(amount)>0 then max(amountdst) else min(amountdst) end AS amountdst
 FROM
 (SELECT
 CASE WHEN deal.direction = 1 THEN '股票做多盈亏' ELSE '股票做空盈亏' END AS typeName,
 symbol.NAME AS symbolName,
 symbol.symbol_cataid AS symbolCataId,
 symbol.profit_currency AS symbolCurrency,
 symbol.source AS symbolSource,
 deal.profit-deal.commission AS amount,
 deal.accountid AS tradeAccountId,
 deal.exec_time AS execTime,
 deal.rate AS rate,
 deal.amountsrc AS amountsrc,
 deal.amountdst AS amountdst,
 CASE WHEN (ord.reserve IS NOT NULL AND ord.reserve != '') THEN ((ord.reserve)::integer)::bigint ELSE ord.id END AS order_reserve
 FROM trade_ix.t_item_deal deal
 LEFT JOIN trade_ix.t_item_order ord ON ord.ID = deal.orderid
 LEFT JOIN trade_ix.t_item_symbol symbol ON symbol.ID = deal.symbolid
 WHERE deal.reason in (4,5,6,7,9,17,20,21) AND deal.accountid= 1230373
 AND symbol.symbol_cataid IN (5)

) t GROUP BY order_reserve
 UNION ALL
 SELECT '交易手续费' AS typeName,
 max(symbolName) symbolName,
 max(symbolCataId) symbolCataId,
 max(symbolCurrency) symbolCurrency,
 max(symbolSource) symbolSource,
 sum(amount) amount,
 max(tradeAccountId) tradeAccountId,
 max(execTime) execTime,
 max(rate) rate,
 case when sum(amount)>0 then min(amountsrc) else max(amountsrc) end AS amountsrc,
 case when sum(amount)>0 then max(amountdst) else min(amountdst) end AS amountdst
 FROM (
select
symbol.NAME AS symbolName,
symbol.symbol_cataid AS symbolCataId,
symbol.profit_currency AS symbolCurrency,
symbol.source AS symbolSource,
deal.amountdst-deal.amountsrc as amount,
deal.accountid AS tradeAccountId,
deal.exec_time AS execTime,
deal.rate AS rate,
deal.amountsrc AS amountsrc,
deal.amountdst AS amountdst,
deal.orderid AS orderid
FROM trade_ix.t_item_deal deal
LEFT JOIN trade_ix.t_item_symbol symbol ON symbol.ID = deal.symbolid
WHERE (deal.reason=1 or deal.reason=2) AND deal.amountdst!=deal.amountsrc
 AND deal.accountid=1230373
 AND symbol.symbol_cataid IN (5)

) tt GROUP BY orderid
 UNION ALL
 select '分红派息' AS typeName,
 symbol.name AS symbolName,
 symbol.symbol_cataid AS symbolCataId,
 symbol.profit_currency AS symbolCurrency,
 symbol.source AS symbolSource,
 deal.amountdst-deal.amountsrc AS amount,
 deal.accountid AS tradeAccountId,
 deal.exec_time AS execTime,
 round(interest.total_pay_amount::numeric/interest.stock_pay_amount::numeric,8) AS rate,
 deal.amountsrc AS amountsrc,
 deal.amountdst AS amountdst
 FROM trade_ix.t_item_deal deal
 LEFT JOIN t_stock_register_interest interest ON interest.pno = deal.proposal_no
 LEFT JOIN trade_ix.t_item_symbol symbol ON interest.stock_code = symbol.id
 WHERE deal.accountid=1230373
 AND interest.stock_pay_amount > 0
 AND deal.proposal_type=350
 AND symbol.symbol_cataid IN (5)
