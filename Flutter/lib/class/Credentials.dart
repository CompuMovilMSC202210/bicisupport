class CredentialsRequest {
  String _email;
  String _password;
  String get email => this._email;

  set email(String value) => this._email = value;

  get password => this._password;

  set password(value) => this._password = value;

  CredentialsRequest(this._email, this._password);

  String toJson() {
    return "{email:$email,password:$password}";
  }
}
