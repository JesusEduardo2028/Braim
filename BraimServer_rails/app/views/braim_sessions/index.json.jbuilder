json.array!(@braim_sessions) do |braim_session|
  json.extract! braim_session, :id
  json.url braim_session_url(braim_session, format: :json)
end
