<?php
// Koneksi ke database
$koneksi = new mysqli("localhost", "root", "", "affogatoaffaircafe");

// Cek koneksi
if ($koneksi->connect_error) {
    die("Koneksi gagal: " . $koneksi->connect_error);
}

// Cek apakah nomor telepon dan password dikirim melalui POST
if (isset($_POST['phone_number'], $_POST['password'])) {
    $phone_number = $_POST['phone_number'];
    $password = $_POST['password'];

    // Siapkan statement SQL untuk mengambil user berdasarkan phone_number
    $stmt = $koneksi->prepare("SELECT password FROM user WHERE phone_number=?");
    $stmt->bind_param("s", $phone_number);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($result->num_rows > 0) {
        $user = $result->fetch_assoc();
        // Verifikasi password
        if (password_verify($password, $user['password'])) {
            echo "Sign in successful";
        } else {
            echo "Invalid phone number or password";
        }
    } else {
        echo "User not found";
    }

    $stmt->close();
} else {
    echo "Phone number and password required";
}

// Tutup koneksi ke database
$koneksi->close();
?>
