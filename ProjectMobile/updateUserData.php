<?php
// Koneksi ke database
$koneksi = new mysqli("localhost", "root", "", "affogatoaffaircafe");

// Cek koneksi
if ($koneksi->connect_error) {
    die("Koneksi gagal: " . $koneksi->connect_error);
}

// Check if POST variables are set
if (isset($_POST['phone_number'], $_POST['name'], $_POST['email'], $_POST['birthdate'], $_POST['password'], $_POST['gender'])) {
    $phone_number = $_POST['phone_number'];
    $name = $_POST['name'];
    $email = $_POST['email'];
    $birthdate = $_POST['birthdate'];
    $password = password_hash($_POST['password'], PASSWORD_DEFAULT); // Hashing the password
    $gender = $_POST['gender'];

    // Add validation and sanitation of inputs here

    $sql = "UPDATE user SET name=?, email=?, birthdate=?, password=?, gender=? WHERE phone_number=?";
    $stmt = $koneksi->prepare($sql);
    $stmt->bind_param("ssssss", $name, $email, $birthdate, $password, $gender, $phone_number);

    if ($stmt->execute()) {
        echo json_encode(["message" => "User data updated successfully"]);
    } else {
        echo json_encode(["message" => "Error updating record: " . $stmt->error]);
    }

    $stmt->close();
} else {
    echo json_encode(["message" => "Required data not provided"]);
}

$koneksi->close();
?>

