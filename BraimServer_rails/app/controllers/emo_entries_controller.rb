class EmoEntriesController < ApplicationController
  before_action :set_emo_entry, only: [:show, :edit, :update, :destroy]

  respond_to :html

  def index
    @emo_entries = EmoEntry.all
    respond_with(@emo_entries)
  end

  def show
    respond_with(@emo_entry)
  end

  def new
    @emo_entry = EmoEntry.new
    respond_with(@emo_entry)
  end

  def edit
  end

  def create
    @emo_entry = EmoEntry.new(emo_entry_params)
    @emo_entry.save
    respond_with(@emo_entry)
  end

  def update
    @emo_entry.update(emo_entry_params)
    respond_with(@emo_entry)
  end

  def destroy
    @emo_entry.destroy
    respond_with(@emo_entry)
  end

  private
    def set_emo_entry
      @emo_entry = EmoEntry.find(params[:id])
    end

    def emo_entry_params
      params[:emo_entry]
    end
end
