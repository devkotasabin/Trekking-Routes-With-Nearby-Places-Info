<?php include("includes/header.php") ?>
<?php include("includes/functions.php")?>
	<title>Trek Route Project</title>
<link rel="stylesheet" type="text/css" href="stylesheets/style.css">
</head>
<?php
   if(isset($_POST['submit']))
   {
	$errors = array();
	$required_fields = array('name','description','rating','difficulty','type','cost','days');
	foreach ($required_fields as $value)
	{
		if(!isset($_POST[$value]) || empty($_POST[$value]))
		{
			$errors[]=$value;
		}
	}
	$name =addslashes($_POST['name']);
	$description =addslashes($_POST['description']);
	$rating =addslashes($_POST['rating']);
	$difficulty =addslashes($_POST['difficulty']);
	$type = addslashes($_POST['type']);
	$cost = addslashes($_POST['cost']);
	$days = addslashes($_POST['days']);
   $query = "INSERT INTO route (name, description, rating, difficulty, type, cost, days) VALUES ('{$name}','{$description}', {$rating},{$difficulty},'{$type}',{$cost},{$days})";

   if (mysql_query($query))
{
	$flag = 2;
}
   else
{
	$flag =1;
}
}
?>
<body>
<div id="wrap">
<div class="hearder-nav">
	<div class="logo">
		TrekRoute Logo
		</div>
<p class="clear-fix">&nbsp;</p>
</div> <!-- End of header-nav -->

<div id="menu">
<ul>
		<li><a href="index.php">Home</a></li>
	<?php
	$route_set = confirm_query("SELECT * FROM route LIMIT 5");
	while($route = mysql_fetch_array($route_set)){
		if ($temp != $route["type"])
		{
			$url = "route-type.php?";
			$temp = urlencode($route["type"]);
			$url .= "type=".$temp;
	 		echo "<li><a href=\"$url\">".ucfirst($route["type"])."</a></li>";
	    } 
		$temp = $route["type"];
		} 
	?>
	</ul> 
</div> <!-- End of menu -->

<div id="content">
<div id="sidebar"><p>	
<h2>Destinations to Visit</h2>
<ul class="sitebar-nav">
<?php
$route_set = confirm_query("SELECT * FROM route");
while($route = mysql_fetch_array($route_set)){
	$upper_route_type = ucfirst($route["type"]);
	$upper_route_name = ucfirst($route["name"]);
	echo "<li><a href=\"pages.php?id={$route["id"]}\">{$upper_route_name} {$upper_route_type}</a></li>";
	}
?>
</ul>
</div> <!-- End of sitebar-->
<div id="main-content"> 
	<?php
	if ($flag==1)
	{
		echo "Errors Entering:";
		echo "<pre>";
		foreach($errors as $value)
		{
			echo "    ->$value<br/>";
		}
		echo "</pre>";
	}
	if ($flag==2)
	{
		echo "<p></b>Data successfully entered to the database</b></p>";
		echo "Please enter another route.<br/><br/>";
	}
	?>
<h2>Add New Route</h2><br/>
<form action="new_route.php" method="post">
	<p>Route Name: <input type="text" name="name" value="" id="name"> </p>
	<p>Description</p> 
	<textarea name="description" value="" id="description" rows="15" cols="85"></textarea>
	<br/>
	<p>Rating: <select name="rating"><option value="
	<?php for($count=0;$count<=10;++$count){ 
	    echo "<option value=\"";
	    echo $count;
	    echo "\">";
	    echo $count;
	    echo "</option>";
	}?>
	</select></p>
	<p>Difficulty: <select name="difficulty"><option value="
		<?php for($count=0;$count<=10;++$count){ 
		    echo "<option value=\"";
		    if ($count>0 && $count<6)
		{
			continue;
		}
		    echo $count;
		    echo "\">";
		    echo $count;
		    echo "</option>";
		}?>
	</select>
	 &nbsp;&nbsp;&nbsp;Please enter 0 for other route other than trekking.
	</p>
	<p>Route Type: <input type="text" name="type" value="" id="type"></p>
	<p>Estimated Cost: $<input type="text" name="cost" value="" id="cost" size="6"></p>
	<p>Estimated Days: <input type="text" name="days" value="" id="days" size="4"></p><br/>
	<input type="submit" name="submit" value="Add Route" />
	</form>
<br/><br/>
<a href="index.php">Cancel</a>
	</div> <!-- End of main content -->
	<p class="clear-fix">&nbsp;</p>
	</div> <!-- End of content-->
<?php include("includes/footer.php")?>