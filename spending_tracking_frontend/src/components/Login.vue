<template>
    <nav class="navbar navbar-expand-lg bg-transparent">
        <div class="container-fluid">
            <a class="navbar-brand" href="#"></a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavAltMarkup"
                aria-controls="navbarNavAltMarkup" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNavAltMarkup">
                <div class="navbar-nav">
                    <a class="nav-link text-white" aria-current="page" href="home">Home</a>
                    <a class="nav-link active text-white" href="login">Login</a>
                </div>
            </div>
        </div>
    </nav>
    <div class="wrapper">
        <div class="form-box login">
            <div class="container">
                <div class="row">
                    <div class="col">
                        <h1>Login</h1>
                    </div>
                </div>
            </div>
            <div class="container">
                <div class="row">
                    <form action="#">
                        <div class="row">
                            <div class="col">
                                <div class="input-box">
                                    <label class="button-label">Email</label><br>
                                    <span class="icon"><ion-icon name="mail-outline"></ion-icon></span>
                                    <input type="email" name="email" required>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col">
                                <div class="input-box">
                                    <label class="button-label">Password</label><br>
                                    <span class="icon"><ion-icon name="lock-closed-outline"></ion-icon></span>
                                    <input type="password" name="password" required>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col">
                                <div class="remember-forgot">
                                    <label><input type="checkbox">Remember me</label><br>
                                    <a href="#">Forgot Password?</a>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col">
                                <div class="login-button"><button type="submit" id="submit-btn"
                                        class="button login-button">Login</button></div>
                                <div class="login-register">
                                    <p>Don't have an account?<br><a href="#" class="register-link">Register</a></p>
                                </div>
                            </div>
                        </div>

                    </form>
                </div>
            </div>


        </div>
    </div>
</template>