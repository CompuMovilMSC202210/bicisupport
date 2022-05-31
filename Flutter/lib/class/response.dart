class Response {
  String _token;
  String _localId;

  Response(this._token, this._localId);

  String get token => this._token;

  set token(String value) => this._token = value;

  get localId => this._localId;

  set localId(value) => this._localId = value;

  Response.fromJson(Map<String, dynamic> json)
      : _token = json['token'],
        _localId = json['localId'];

  Map<String, dynamic> toJson() =>
    {
      'token': token,
      'localId': localId,
    };
}
