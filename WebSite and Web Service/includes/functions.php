<?php 
function confirm_query($query)
{
	if($data_set = mysql_query($query))
	{
		return $data_set;
	}
	else
	{
		echo "Error! Mysql query failed";
	}
}
?>