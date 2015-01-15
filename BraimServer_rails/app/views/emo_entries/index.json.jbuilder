json.array!(@emo_entries) do |emo_entry|
  json.extract! emo_entry, :id
  json.url emo_entry_url(emo_entry, format: :json)
end
