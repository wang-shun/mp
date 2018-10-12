/**
# 操作符
# _eq 等于
# _notNull 非空
# _null 空
# _gt >
# _ge >=
# _lt <
# _le <=
# _like 包含
# _notlike 不包含
*/
function _eq(value, test) {
	return value == test;
}

function _notNull(value, test) {
	return (value != null) == test;
}

function _null(value, test) {
	return (value == null) == test;
}

function _gt(value, test) {
	return value > test;
}

function _ge(value, test) {
	return value >= test;
}

function _lt(value, test) {
	return value < test;
}

function _le(value, test) {
	return value <= test;
}

function _like(value, test) {
	return (value + "").indexOf(test) >=0;
}

function _notLike(value, test) {
	return (value + "").indexOf(test) < 0;
}