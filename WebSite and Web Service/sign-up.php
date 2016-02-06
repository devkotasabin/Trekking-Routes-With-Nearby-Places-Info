<?php include("includes/header.php"); ?>
<?php include("includes/functions.php"); ?>
	<title>Trekroute Project</title>
<link rel="stylesheet" type="text/css" href="stylesheets/style.css">
</head>
<?php 
if (isset($_POST['submit']))
{
    $name=$_POST['name'];
	$username=$_POST['username'];
	$password = $_POST['password'];
	$username = addslashes($username);
	$name = addslashes($name);
	$password = addslashes($password);
	$hased_password = sha1($password);
	$query="SELECT * from users";
	$user_set = confirm_query("SELECT * FROM users");
	while ($user = mysql_fetch_array($user_set))
	{
		if ($user['username']==$username)
		{
			$flag =1;
			break;
		}
	}
	if ($flag !=1)
	{
    	$query = "INSERT INTO users (name, username, password ) VALUES ('{$name}','{$username}', '{$hased_password}')";
    	if(!$query)
 	{
		echo "Query failed";
	}
    mysql_query($query);
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
<ul class="sitebar-nav">

</ul>
</div> <!-- End of sitebar-->
<?php
if ($flag ==1)
{
	echo " Please select another username";
}
?>
<div id="main-content"> 
	<h2>Sign Up</h2>
<form action="sign-up.php" method="post">
Name:  &nbsp;&nbsp;&nbsp;&nbsp;    <input type="text" name="name" maxlength="30" value=""><br/>
<p>Username: <input type="text" name="username" maxlength="30" value=""></p>
<p>Password: &nbsp;<input type="password" name="password" maxlength="30" value=""></p><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" name="submit" value="Create user" />
</form>	
</p>
	</div> <!-- End of main content -->
	<p class="clear-fix">&nbsp;</p>
	</div> <!-- End of content-->
<?php include("includes/footer.php"); ?>