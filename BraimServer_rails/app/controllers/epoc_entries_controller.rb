class EpocEntriesController < ApplicationController
  before_action :set_epoc_entry, only: [:show, :edit, :update, :destroy]

  respond_to :html

  def index
    @epoc_entries = epocEntry.all
    respond_with(@epoc_entries)
  end

  def show
    respond_with(@epoc_entry)
  end

  def new
    @epoc_entry = epocEntry.new
    respond_with(@epoc_entry)
  end

  def edit
  end

  def create
    @epoc_entry = epocEntry.new(epoc_entry_params)
    @epoc_entry.save
    respond_with(@epoc_entry)
  end

  def update
    @epoc_entry.update(epoc_entry_params)
    respond_with(@epoc_entry)
  end

  def destroy
    @epoc_entry.destroy
    respond_with(@epoc_entry)
  end

  private
    def set_epoc_entry
      @epoc_entry = epocEntry.find(params[:id])
    end

    def epoc_entry_params
      params[:epoc_entry]
    end
end
