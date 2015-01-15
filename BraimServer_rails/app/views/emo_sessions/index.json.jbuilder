json.array!(@emo_sessions) do |emo_session|
  json.extract! emo_session, :id
  json.url emo_session_url(emo_session, format: :json)
end
